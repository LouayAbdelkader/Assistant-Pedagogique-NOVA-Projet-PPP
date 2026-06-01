# routes/predict.py
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from ..assistant import repondre
from typing import Optional

router = APIRouter()

class QuestionRequest(BaseModel):
    question: str

class ReponseResponse(BaseModel):
    chap_id: Optional[int] = None
    chap_name: Optional[str] = None
    slide_id: Optional[int] = None
    section: Optional[str] = None
    category: Optional[str] = None
    score: float = 0.0
    reponse: str

@router.post("/predict", response_model=ReponseResponse)
async def predict(request: QuestionRequest):
    try:
        result = repondre(request.question)
        return ReponseResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erreur lors du traitement : {str(e)}")