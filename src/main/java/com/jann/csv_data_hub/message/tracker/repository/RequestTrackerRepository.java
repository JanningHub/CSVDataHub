package com.jann.csv_data_hub.message.tracker.repository;

import com.jann.csv_data_hub.message.tracker.domain.RequestTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTrackerRepository extends JpaRepository<RequestTracker, String> {
}
