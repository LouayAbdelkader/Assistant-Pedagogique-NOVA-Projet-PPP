# assistant.py
import json
import re
import numpy as np
from sentence_transformers import SentenceTransformer
from .config import SLIDE_EMBEDDINGS_PATH, SLIDES_METADATA_PATH

# Chargement global (une fois au démarrage du serveur)
print("Chargement du modèle SentenceTransformer...")
model = SentenceTransformer("intfloat/multilingual-e5-base")
print("Modèle chargé.")

print("Chargement des embeddings des slides...")
slide_embeddings = np.load(SLIDE_EMBEDDINGS_PATH)
with open(SLIDES_METADATA_PATH, "r", encoding="utf-8") as f:
    slides_metadata = json.load(f)
print(f"Embeddings : {slide_embeddings.shape} | Métadonnées : {len(slides_metadata)} slides.")

def split_sentences(text: str) -> list:
    """Découpe un texte en phrases."""
    sentences = re.split(r'(?<=[.!?])\s+', text.strip())
    sentences = [re.sub(r'\s+', ' ', s).strip() for s in sentences if s.strip()]
    return sentences

def repondre(question: str) -> dict:
    """
    Retourne la réponse extraite du slide le plus pertinent.
    Sortie : {"reponse": str, "section": str}
    """
    # Encodage de la question
    q_emb = model.encode([f"query: {question}"], normalize_embeddings=True)
    # Similarité cosinus avec tous les slides
    scores = np.dot(slide_embeddings, q_emb.T).flatten()
    best_idx = scores.argmax()
    slide = slides_metadata[best_idx]
    content = slide.get("slide_content", "")
    section = slide.get("section", "")

    if not content:
        reponse = "[Pas de texte exploitable]"
    else:
        phrases = split_sentences(content)
        if not phrases:
            reponse = content[:300]
        else:
            phr_embs = model.encode([f"passage: {p}" for p in phrases], normalize_embeddings=True)
            phr_scores = np.dot(phr_embs, q_emb.T).flatten()
            best_phrase_idx = phr_scores.argmax()
            reponse = phrases[best_phrase_idx]
    return {"reponse": reponse, "section": section}