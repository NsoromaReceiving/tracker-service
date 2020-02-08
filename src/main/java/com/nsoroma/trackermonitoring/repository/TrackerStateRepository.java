package com.nsoroma.trackermonitoring.repository;

import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TrackerStateRepository extends MongoRepository<TrackerState, String> {

}
