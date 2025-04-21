package com.example.demo.scheduler;

import com.example.demo.model.OutboxEvent;
import com.example.demo.Service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {
    private final OutboxService outboxService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    @Transactional
    public void processOutbox() {
        List<OutboxEvent> unpublishedEvents = outboxService.getUnpublishedEvents();

        if (!unpublishedEvents.isEmpty()) {
            log.info("Found {} unpublished events", unpublishedEvents.size());

            // Publish events to Kafka
            unpublishedEvents.forEach(event -> {
                String topic = "orders." + event.getEventType().toLowerCase();
                kafkaTemplate.send(topic, event.getPayload());
                log.info("Published event {} to topic {}", event.getEventId(), topic);
            });

            // Mark events as published
            List<Long> publishedIds = unpublishedEvents.stream()
                    .map(OutboxEvent::getId)
                    .collect(Collectors.toList());

            outboxService.markEventsAsPublished(publishedIds);
            log.info("Marked {} events as published", publishedIds.size());
        }
    }
}