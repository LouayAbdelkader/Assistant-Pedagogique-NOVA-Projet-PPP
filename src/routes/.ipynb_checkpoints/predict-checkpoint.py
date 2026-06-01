# routes/predict.py
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from ..assistant import repondre

router = APIRouter()

class QuestionRequest(BaseModel):
    question: str

class ReponseResponse(BaseModel):
    reponse: str
    section: str

@router.post("/predict", response_model=ReponseResponse)
async def predict(request: QuestionRequest):
    try:
        result = repondre(request.question)
        return ReponseResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erreur lors du traitement : {str(e)}")