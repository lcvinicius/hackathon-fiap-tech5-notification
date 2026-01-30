package com.notification.repository;

import com.notification.model.Subscriber;

import java.util.List;

public interface SubscriberRepository {

    List<Subscriber> findSubscribers(String medicineId, String ubsId) throws Exception;
}
