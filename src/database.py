# database.py
import mysql.connector
from mysql.connector import Error
from typing import List, Tuple
from .config import DB_CONFIG

def fetch_all_questions_with_counts() -> List[Tuple[str, int]]:
    """
    Retourne toutes les questions avec leur nombre d'occurrences (tous utilisateurs).
    Exemple : [("bonjour", 10), ("c'est quoi python", 3)]
    """
    conn = None
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        cursor.execute("""
            SELECT question, COUNT(*) as nb
            FROM interactions
            WHERE question IS NOT NULL AND question != ''
            GROUP BY question
        """)
        return cursor.fetchall()
    except Error as e:
        print(f"Erreur MySQL : {e}")
        return []
    finally:
        if conn and conn.is_connected():
            cursor.close()
            conn.close()

def fetch_questions_with_counts_by_user(user_id: int) -> List[Tuple[str, int]]:
    """
    Retourne les questions posées par un utilisateur spécifique avec leur nombre d'occurrences.
    """
    conn = None
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        cursor.execute("""
            SELECT i.question, COUNT(*) as nb
            FROM interactions i
            JOIN session_cours s ON i.session_id = s.id
            WHERE s.user_id = %s
              AND i.question IS NOT NULL AND i.question != ''
            GROUP BY i.question
        """, (user_id,))
        return cursor.fetchall()
    except Error as e:
        print(f"Erreur MySQL : {e}")
        return []
    finally:
        if conn and conn.is_connected():
            cursor.close()
            conn.close()