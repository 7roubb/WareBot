package com.osama.warebot.robots;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RobotTelemetryRepository extends MongoRepository<RobotTelemetry, String> {
    List<RobotTelemetry> findByRobotIdOrderByTimestampDesc(String robotId);
    Page<RobotTelemetry> findByRobotId(String robotId, Pageable pageable);
    List<RobotTelemetry> findTop10ByRobotIdOrderByTimestampDesc(String robotId);
    void deleteByRobotId(String robotId);
}