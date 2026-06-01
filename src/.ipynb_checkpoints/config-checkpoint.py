# config.py
import os
from pathlib import Path

# Base du projet (le dossier parent de src)
BASE_DIR = Path(__file__).resolve().parent.parent

# Dossier des données
DATA_DIR = BASE_DIR / "data" / "3-numerical"
SLIDE_EMBEDDINGS_PATH = DATA_DIR / "slide_embeddings.npy"
SLIDES_METADATA_PATH = DATA_DIR / "slides_metadata.json"

# Configuration MySQL (XAMPP)
DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': '',
    'database': 'assistant_ts_db'
}

# Cache des top questions
CACHE_DURATION = 30  # par secondes