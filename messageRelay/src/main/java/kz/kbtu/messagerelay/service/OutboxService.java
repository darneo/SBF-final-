package kz.kbtu.messagerelay.service;

import kz.kbtu.messagerelay.model.*;
import kz.kbtu.messagerelay.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;

    public OutboxService(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    public List<OutboxEvent> getUnpublishedEvents() {
        return outboxEventRepository.findByPublishedFalse();
    }

    public void markEventsAsPublished(List<Long> ids) {
        outboxEventRepository.markAsPublished(ids);
    }
}
