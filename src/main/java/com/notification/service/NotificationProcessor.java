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

    public NotificationProcessor(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public List<Subscriber> findSubscribers(MedicationArrivedEvent event) throws Exception {
        String medicineId = event.getMedicineId();
        String ubsId = event.getUbsId();
        List<Subscriber> subscribers = subscriberRepository.findSubscribers(medicineId, ubsId);
        logger.info("subscribers found; count={}; medicineId={}; ubsId={}", subscribers.size(), medicineId, ubsId);
        return subscribers;
    }
}
