# main.py
from fastapi import FastAPI
from src.routes.predict import router as predict_router
from src.routes.top_questions import router as top_router

app = FastAPI(title="Assistant Pédagogique API", version="1.0")

app.include_router(predict_router)
app.include_router(top_router)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)