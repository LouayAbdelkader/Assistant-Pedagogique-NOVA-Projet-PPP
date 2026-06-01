# database.py
import mysql.connector
from mysql.connector import Error
from typing import List
from .config import DB_CONFIG

def fetch_all_questions() -> List[str]:
    """Récupère toutes les questions distinctes de la table interaction."""
    conn = None
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        cursor.execute("SELECT DISTINCT question FROM interaction WHERE question IS NOT NULL AND question != ''")
        rows = cursor.fetchall()
        return [row[0] for row in rows]
    except Error as e:
        print(f"Erreur MySQL : {e}")
        return []
    finally:
        if conn and conn.is_connected():
            cursor.close()
            conn.close()

def fetch_questions_by_user_id(user_id: int) -> List[str]:
    """
    Récupère toutes les questions distinctes posées par un utilisateur via son id.
    """
    conn = None
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        query = """
            SELECT DISTINCT i.question
            FROM interaction i
            JOIN session_cours s ON i.session_id = s.id
            WHERE s.user_id = %s
              AND i.question IS NOT NULL
              AND i.question != ''
        """
        cursor.execute(query, (user_id,))
        rows = cursor.fetchall()
        return [row[0] for row in rows]
    except Error as e:
        print(f"Erreur MySQL : {e}")
        return []
    finally:
        if conn and conn.is_connected():
            cursor.close()
            conn.close()