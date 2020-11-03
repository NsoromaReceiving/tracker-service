package com.nsoroma.trackermonitoring.repository;

import com.nsoroma.trackermonitoring.model.customer.SlimCustomer;
import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<SlimCustomer, String> {
}
