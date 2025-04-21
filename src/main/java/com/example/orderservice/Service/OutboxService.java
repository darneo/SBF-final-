package com.example.orderservice.Service;
import com.example.orderservice.model.OutboxEvent;
import com.example.orderservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxEventRepository outboxEventRepository;

    public List<OutboxEvent> getUnpublishedEvents() {
        return outboxEventRepository.findByPublishedFalse();
    }

    @Transactional
    public void markEventsAsPublished(List<Long> ids) {
        outboxEventRepository.markAsPublished(ids);
    }
}