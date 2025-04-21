package com.example.orderservice.repository;
import com.example.orderservice.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByPublishedFalse();

    @Modifying
    @Transactional
    @Query("UPDATE OutboxEvent e SET e.published = true WHERE e.id IN ?1")
    void markAsPublished(List<Long> ids);
}