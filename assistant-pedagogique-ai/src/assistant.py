# assistant.py
import json
import re
import unicodedata
import numpy as np
from sentence_transformers import SentenceTransformer
import ollama
from .config import SLIDE_EMBEDDINGS_PATH, SLIDES_METADATA_PATH

# ---------------------------------------------------------------------------
# Chargement global (une fois au démarrage du serveur)
# ---------------------------------------------------------------------------

print("Chargement du modèle SentenceTransformer...")
model = SentenceTransformer("intfloat/multilingual-e5-base")
print("Modèle chargé.")

print("Chargement des embeddings des slides...")
slide_embeddings = np.load(SLIDE_EMBEDDINGS_PATH)
with open(SLIDES_METADATA_PATH, "r", encoding="utf-8") as f:
    slides_metadata = json.load(f)
print(f"Embeddings : {slide_embeddings.shape} | Métadonnées : {len(slides_metadata)} slides.")

OLLAMA_MODEL = "llama3.2"

# ---------------------------------------------------------------------------
# Catégories
# ---------------------------------------------------------------------------

CATEGORIES = {
    "definition": [
        "definir", "definition", "qu'est ce que", "qu est ce que",
        "qu'est-ce qu'", "c'est quoi", "c est quoi", "que signifie", "signifie"
    ],
    "formule": [
        "formule", "equation", "calcul", "calculer", "expression", "relation"
    ],
    "exemple": [
        "exemple", "illustrer", "application", "cas pratique"
    ],
    "remarque": [
        "remarque", "attention", "important", "particularite", "limitation"
    ],
    "image": [
        "image", "figure", "schema", "graphe", "graphique", "courbe"
    ]
}

SALUTATIONS = {
    "bonjour", "bonsoir", "salut", "hello", "hi", "hey",
    "salam", "coucou", "yo", "allo", "allô"
}
# ---------------------------------------------------------------------------
# Fonctions utilitaires
# ---------------------------------------------------------------------------

def normalize(text: str) -> str:
    """Minuscules, suppression des accents, normalisation des espaces."""
    text = text.lower()
    text = ''.join(
        c for c in unicodedata.normalize('NFD', text)
        if unicodedata.category(c) != 'Mn'
    )
    text = re.sub(r'\s+', ' ', text)
    return text.strip()


def classify_question(question: str) -> str:
    """Retourne la catégorie détectée ou 'unknown'."""
    q = normalize(question)
    for category, keywords in CATEGORIES.items():
        for kw in keywords:
            if normalize(kw) in q:
                return category
    return "unknown"


def split_sentences(text: str) -> list:
    """Découpe un texte en phrases."""
    if not text:
        return []
    sentences = re.split(r'(?<=[.!?])\s+', text.strip())
    sentences = [re.sub(r'\s+', ' ', s).strip() for s in sentences if s.strip()]
    return sentences


def reformuler_reponse(question: str, reponse_brute: str) -> str:
    """
    Reformule reponse_brute via llama3.2 (Ollama) pour la rendre claire et
    cohérente, SANS ajouter ni modifier l'information d'origine.
    En cas d'erreur, retourne reponse_brute tel quel.
    """
    if not reponse_brute or reponse_brute == "[Pas de texte exploitable]":
        return reponse_brute

    prompt = f"""Tu es un assistant pédagogique. Tu reçois une question et une réponse extraite d'un cours.
Ta seule tâche est de reformuler cette réponse pour qu'elle soit claire et fluide.

Règles STRICTES :
- Si la réponse extraite n'a pas de lien clair avec la question, réponds uniquement avec : "Je ne connais pas la réponse dans le cours."
- Si la réponse est pertinente, reformule-la pour qu'elle soit claire et fluide.
- Ne jamais ajouter d'informations qui ne sont pas dans la réponse fournie.
- Ne jamais modifier le sens ou les faits de la réponse.
- Ne jamais inventer d'exemples, de définitions ou d'explications supplémentaires.
- Réponds dans la même langue que la question.

Question : {question}
Réponse à reformuler : {reponse_brute}
Réponse reformulée :"""

    try:
        response = ollama.chat(
            model=OLLAMA_MODEL,
            messages=[{"role": "user", "content": prompt}],
            options={"temperature": 0.1},
        )
        return response["message"]["content"].strip()
    except Exception as e:
        print(f"[Ollama] Erreur lors de la reformulation : {e}")
        return reponse_brute


def is_salutation(question: str) -> bool:
    """Détecte si la question est une simple salutation."""
    q = normalize(question)
    tokens = q.split()
    return any(token in SALUTATIONS for token in tokens)

# ---------------------------------------------------------------------------
# Fonction principale
# ---------------------------------------------------------------------------

def repondre(
    question: str,
    top_k: int = 1,
    use_sentence_extraction: bool = True,
    domain_threshold: float = 0.50,
    slide_threshold: float = 0.75,
    phrase_threshold: float = 0.65
) -> dict:
    """
    Retourne la réponse extraite du slide le plus pertinent,
    reformulée via Ollama si un slide pertinent est trouvé.
    """

    # -----------------------------------------------------------------------
    # Détection des salutations
    # -----------------------------------------------------------------------

    if is_salutation(question):
        return {
            "chap_id": None, "chap_name": None, "slide_id": None,
            "section": None, "category": "salutation", "score": 0.0,
            "reponse": "Hello ! Je suis votre assistant pédagogique. Posez-moi une question sur le cours."
        }
    
    # -----------------------------------------------------------------------
    # Encodage de la question (une seule fois)
    # -----------------------------------------------------------------------

    q_emb = model.encode([f"query: {question}"], normalize_embeddings=True)

    # -----------------------------------------------------------------------
    # Rejet hors domaine — sémantique, indépendant de la catégorie
    # -----------------------------------------------------------------------

    max_score = float(np.dot(slide_embeddings, q_emb.T).max())
    if max_score < domain_threshold:
        return {
            "chap_id": None, "chap_name": None, "slide_id": None,
            "section": None, "category": "unknown", "score": 0.0,
            "reponse": "Je ne connais pas la réponse dans le cours."
        }

    # -----------------------------------------------------------------------
    # Catégorisation — sert uniquement au filtrage, jamais au rejet
    # -----------------------------------------------------------------------

    question_category = classify_question(question)

    # -----------------------------------------------------------------------
    # Filtrage par catégorie
    # -----------------------------------------------------------------------

    if question_category != "unknown":
        candidate_indices = [
            i for i, slide in enumerate(slides_metadata)
            if slide.get("category") == question_category
        ]
        if len(candidate_indices) == 0:
            candidate_indices = list(range(len(slides_metadata)))
    else:
        candidate_indices = list(range(len(slides_metadata)))

    # -----------------------------------------------------------------------
    # Similarité des slides candidats
    # -----------------------------------------------------------------------

    candidate_embeddings = slide_embeddings[candidate_indices]
    scores = np.dot(candidate_embeddings, q_emb.T).flatten()
    best_local_idx = scores.argmax()
    best_slide_score = float(scores[best_local_idx])

    # -----------------------------------------------------------------------
    # Fallback global si score insuffisant dans la catégorie filtrée
    # -----------------------------------------------------------------------

    if best_slide_score < slide_threshold and len(candidate_indices) < len(slides_metadata):
        candidate_indices = list(range(len(slides_metadata)))
        candidate_embeddings = slide_embeddings[candidate_indices]
        scores = np.dot(candidate_embeddings, q_emb.T).flatten()
        best_local_idx = scores.argmax()
        best_slide_score = float(scores[best_local_idx])

    # -----------------------------------------------------------------------
    # Rejet final — seulement après avoir cherché partout
    # -----------------------------------------------------------------------

    if best_slide_score < slide_threshold:
        return {
            "chap_id": None, "chap_name": None, "slide_id": None,
            "section": None, "category": question_category,
            "score": best_slide_score,
            "reponse": "Je ne connais pas la réponse dans le cours."
        }

    # -----------------------------------------------------------------------
    # Top K — extraction de la meilleure phrase par slide
    # -----------------------------------------------------------------------

    best_local_indices = scores.argsort()[::-1][:top_k]
    resultats = []

    for local_idx in best_local_indices:

        idx = candidate_indices[local_idx]
        slide = slides_metadata[idx]
        content = slide.get("slide_content", "")
        slide_score = float(scores[local_idx])

        if not content:
            reponse_brute = "[Pas de texte exploitable]"
            phrase_score = 0.0

        elif use_sentence_extraction:
            phrases = split_sentences(content)

            if len(phrases) == 0:
                reponse_brute = content[:300]
                phrase_score = slide_score
            else:
                phr_embs = model.encode(
                    [f"passage: {p}" for p in phrases],
                    normalize_embeddings=True
                )
                phr_scores = np.dot(phr_embs, q_emb.T).flatten()
                best_phrase_idx = phr_scores.argmax()
                phrase_score = float(phr_scores[best_phrase_idx])
                reponse_brute = phrases[best_phrase_idx]

        else:
            reponse_brute = content[:500]
            if len(content) > 500:
                reponse_brute += "..."
            phrase_score = slide_score

        if phrase_score < phrase_threshold:
            continue

        # -------------------------------------------------------------------
        # Reformulation via Ollama
        # -------------------------------------------------------------------

        reponse_finale = reformuler_reponse(question, reponse_brute)

        resultats.append({
            "chap_id": slide.get("chap_id"),
            "chap_name": slide.get("chap_name"),
            "slide_id": slide.get("slide_id"),
            "section": slide.get("section"),
            "category": question_category,
            "score": round(phrase_score, 4),
            "reponse": reponse_finale
        })

    if not resultats:
        return {
            "chap_id": None, "chap_name": None, "slide_id": None,
            "section": None, "category": question_category, "score": 0.0,
            "reponse": "Je ne connais pas la réponse dans le cours."
        }

    return resultats[0]