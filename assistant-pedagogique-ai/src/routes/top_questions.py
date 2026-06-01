# routes/top_questions.py
from fastapi import APIRouter, Query
from typing import List
from pydantic import BaseModel
from ..stats import get_top_questions, get_top_questions_by_user

router = APIRouter()

class TopQuestionItem(BaseModel):
    question: str
    count: int

@router.get("/top-questions", response_model=List[TopQuestionItem])
async def top_questions(k: int = Query(5, ge=1, le=20)):
    """
    Retourne les K questions les plus fréquentes (regroupées sémantiquement).
    """
    return get_top_questions(k)

@router.get("/top-questions/user/{user_id}", response_model=List[TopQuestionItem])
async def top_questions_by_user_id(user_id: int, k: int = Query(5, ge=1, le=20)):
    """Retourne les K questions les plus fréquentes posées par l'utilisateur (id)."""
    return get_top_questions_by_user(user_id, k)