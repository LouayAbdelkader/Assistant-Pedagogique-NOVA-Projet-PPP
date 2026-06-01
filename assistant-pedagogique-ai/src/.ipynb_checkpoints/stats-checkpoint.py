# stats.py
import time
import numpy as np
from typing import List, Dict, Tuple 
from .assistant import model
from .database import fetch_all_questions, fetch_questions_by_user_id
from .config import CACHE_DURATION

_cache_top_questions = []
_cache_time = 0.0

def group_similar_questions(questions: List[str], threshold: float = 0.85) -> List[dict]:
    """Regroupe les questions sémantiquement proches. Retourne liste triée par taille de groupe."""
    if not questions:
        return []

    # Encodage avec préfixe "query:"
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
        groups.append({
            'representative': questions[group_indices[0]],
            'count': len(group_indices)
        })
    # Trier par nombre de questions (décroissant)
    groups.sort(key=lambda x: x['count'], reverse=True)
    return groups

def get_top_questions(k: int = 5) -> List[str]:
    """
    Retourne les K questions les plus représentatives (regroupées sémantiquement).
    Utilise un cache qui expire après CACHE_DURATION secondes.
    """
    global _cache_top_questions, _cache_time
    now = time.time()
    if now - _cache_time > CACHE_DURATION or not _cache_top_questions:
        all_questions = fetch_all_questions()
        groups = group_similar_questions(all_questions)
        _cache_top_questions = [g['representative'] for g in groups[:k]]
        _cache_time = now
    return _cache_top_questions[:k]

# Cache spécifique par utilisateur : {user_id: (timestamp, [top_questions])}
_user_cache: Dict[int, Tuple[float, List[str]]] = {}

def get_top_questions_by_user(user_id: int, k: int = 5) -> List[str]:
    now = time.time()
    if user_id in _user_cache:
        cached_time, cached_list = _user_cache[user_id]
        if now - cached_time < CACHE_DURATION:
            return cached_list[:k]

    questions = fetch_questions_by_user_id(user_id)
    if not questions:
        return []
    groups = group_similar_questions(questions)
    top = [g['representative'] for g in groups[:k]]
    _user_cache[user_id] = (now, top)
    return top