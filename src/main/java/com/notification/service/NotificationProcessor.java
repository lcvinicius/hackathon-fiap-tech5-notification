package com.notification.service;

import com.notification.model.MedicationArrivedEvent;
import com.notification.model.Subscriber;
import com.notification.repository.SubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProcessor.class);

    private final SubscriberRepository subscriberRepository;
    private final NotificationMessageBuilder messageBuilder;
    private final NotificationPublisher publisher;

    public NotificationProcessor(SubscriberRepository subscriberRepository,
                                 NotificationMessageBuilder messageBuilder,
                                 NotificationPublisher publisher) {
        this.subscriberRepository = subscriberRepository;
        this.messageBuilder = messageBuilder;
        this.publisher = publisher;
    }

    public void process(MedicationArrivedEvent event) throws Exception {
        String medicineId = event.getMedicineId();
        String ubsId = event.getUbsId();
        List<Subscriber> subscribers = subscriberRepository.findSubscribers(medicineId, ubsId);
        logger.info("subscribers found; count={}; medicineId={}; ubsId={}", subscribers.size(), medicineId, ubsId);

        String message = messageBuilder.buildMessage(event);
        int sent = 0;
        int failed = 0;

        for (Subscriber subscriber : subscribers) {
            try {
                publisher.publishSms(subscriber.getPhone(), message);
                sent++;
                logger.info("notification sent; phone={}", subscriber.getPhone());
            } catch (Exception e) {
                failed++;
                logger.error("notification failed; phone={}", subscriber.getPhone(), e);
            }
        }

        logger.info("notification summary; total={}; sent={}; failed={}", subscribers.size(), sent, failed);
    }
}
