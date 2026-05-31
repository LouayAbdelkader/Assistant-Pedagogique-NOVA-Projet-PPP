package com.assistant.repository;

import com.assistant.entity.NavigationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavigationLogRepository extends JpaRepository<NavigationLog, Long> {
    // Récupère l'historique d'une session, du plus récent au plus ancien
    List<NavigationLog> findBySessionIdOrderByTimestampDesc(Long sessionId);
}