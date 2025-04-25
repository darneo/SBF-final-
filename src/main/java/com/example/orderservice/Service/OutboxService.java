package com.example.orderservice.Service;

import com.example.orderservice.model.OutboxEvent;
import com.example.orderservice.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;

    // Явный конструктор вместо Lombok @RequiredArgsConstructor
    public OutboxService(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    public List<OutboxEvent> getUnpublishedEvents() {
        return outboxEventRepository.findByPublishedFalse();
    }

    @Transactional
    public void markEventsAsPublished(List<Long> ids) {
        outboxEventRepository.markAsPublished(ids);
    }
}
