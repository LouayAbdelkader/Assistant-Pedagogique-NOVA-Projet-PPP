# stats.py
import time
import numpy as np
from typing import List, Dict, Tuple
from .assistant import model
from .database import fetch_all_questions_with_counts, fetch_questions_with_counts_by_user
from .config import CACHE_DURATION

_cache_top_questions = []   # liste de dict {"question": str, "count": int}
_cache_time = 0.0
_user_cache: Dict[int, Tuple[float, List[dict]]] = {}

def group_similar_questions_with_counts(questions_with_counts: List[Tuple[str, int]], threshold: float = 0.85) -> List[dict]:
    """
    Regroupe sémantiquement les questions et additionne leurs occurrences.
    Entrée : liste de (question, count)
    Sortie : liste de [{"representative": str, "total_count": int}, ...] triée par total_count décroissant.
    """
    if not questions_with_counts:
        return []
    questions = [q for q, _ in questions_with_counts]
    counts = {q: c for q, c in questions_with_counts}

    # Encodage des questions
    q_embs = model.encode([f"query: {q}" for q in questions], normalize_embeddings=True, show_progress_bar=False)
    sim_matrix = np.dot(q_embs, q_embs.T)

    visited = set()
    groups = []
    for i, q in enumerate(questions):
        if i in visited:
            continue
        group_indices = [i]
        visited.add(i)
        for j in range(len(questions)):
            if j != i and j not in visited and sim_matrix[i, j] >= threshold:
                group_indices.append(j)
                visited.add(j)
        total_count = sum(counts[questions[idx]] for idx in group_indices)
        groups.append({
            'representative': questions[group_indices[0]],
            'total_count': total_count
        })
    groups.sort(key=lambda x: x['total_count'], reverse=True)
    return groups

def get_top_questions(k: int = 5) -> List[dict]:
    """
    Retourne les K questions les plus fréquentes (globales) avec leur nombre d'occurrences.
    Exemple de sortie : [{"question": "bonjour", "count": 10}, ...]
    """
    global _cache_top_questions, _cache_time
    now = time.time()
    if now - _cache_time > CACHE_DURATION or not _cache_top_questions:
        all_questions_counts = fetch_all_questions_with_counts()
        groups = group_similar_questions_with_counts(all_questions_counts)
        _cache_top_questions = [
            {"question": g['representative'], "count": g['total_count']}
            for g in groups[:k]
        ]
        _cache_time = now
    return _cache_top_questions[:k]

def get_top_questions_by_user(user_id: int, k: int = 5) -> List[dict]:
    """
    Retourne les K questions les plus fréquentes posées par un utilisateur spécifique.
    """
    now = time.time()
    if user_id in _user_cache:
        cached_time, cached_list = _user_cache[user_id]
        if now - cached_time < CACHE_DURATION:
            return cached_list[:k]

    questions_counts = fetch_questions_with_counts_by_user(user_id)
    if not questions_counts:
        return []
    groups = group_similar_questions_with_counts(questions_counts)
    top = [
        {"question": g['representative'], "count": g['total_count']}
        for g in groups[:k]
    ]
    _user_cache[user_id] = (now, top)
    return top