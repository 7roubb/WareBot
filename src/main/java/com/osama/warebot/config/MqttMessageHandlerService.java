package com.osama.warebot.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osama.warebot.robots.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class MqttMessageHandlerService implements MessageHandler {

    @Autowired
    private RobotTelemetryRepository telemetryRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            String payload = message.getPayload().toString();
            String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);

            log.info("Received MQTT message from topic: {}", topic);
            log.debug("Message payload: {}", payload);

            if (topic.contains("/telemetry")) {
                processTelemetryMessage(payload, topic);
            } else if (topic.contains("/status")) {
                processStatusMessage(payload, topic);
            }

        } catch (Exception e) {
            log.error("Error processing MQTT message", e);
        }
    }

    private void processTelemetryMessage(String payload, String topic) {
        try {
            String robotId = extractRobotIdFromTopic(topic);
            if (robotId == null) {
                log.warn("Could not extract robot ID from topic: {}", topic);
                return;
            }

            JsonNode json = objectMapper.readTree(payload);

            RobotTelemetry telemetry = RobotTelemetry.builder()
                    .id(UUID.randomUUID().toString())
                    .robotId(robotId)
                    .speed(json.has("speed") ? json.get("speed").asDouble() : 0.0)
                    .batteryLife(json.has("battery_life") ? json.get("battery_life").asDouble() : 0.0)
                    .charging(json.has("is_charging") ? json.get("is_charging").asBoolean() : false)
                    .cpuUsage(json.has("cpu_usage") ? json.get("cpu_usage").asDouble() : 0.0)
                    .temperature(json.has("temperature") ? json.get("temperature").asDouble() : 0.0)
                    .memoryUsage(json.has("memory_usage") ? json.get("memory_usage").asDouble() : 0.0)
                    .networkStatus(json.has("network_status") ? json.get("network_status").asText() : "UNKNOWN")
                    .location(json.has("location") ? json.get("location").asText() : "UNKNOWN")
                    .currentTask(json.has("current_task") ? json.get("current_task").asText() : "IDLE")
                    .timestamp(Instant.now())
                    .build();

            telemetryRepository.save(telemetry);
            log.info("Saved telemetry for robot: {}", robotId);

        } catch (Exception e) {
            log.error("Error processing telemetry message", e);
        }
    }

    private void processStatusMessage(String payload, String topic) {
        try {
            String robotId = extractRobotIdFromTopic(topic);
            if (robotId == null) return;

            JsonNode json = objectMapper.readTree(payload);

            // Update robot status in the main robot collection
            Robot robot = robotRepository.findById(robotId)
                    .orElse(Robot.builder()
                            .id(robotId)
                            .name(json.has("name") ? json.get("name").asText() : "Robot-" + robotId)
                            .createdAt(Instant.now())
                            .build());

            robot.setAvailable(json.has("available") ? json.get("available").asBoolean() : true);
            robot.setStatus(parseRobotStatus(json));
            robot.setUpdatedAt(Instant.now());

            robotRepository.save(robot);
            log.info("Updated status for robot: {}", robotId);

        } catch (Exception e) {
            log.error("Error processing status message", e);
        }
    }
    private RobotStatus parseRobotStatus(JsonNode json) {
        if (!json.has("status")) {
            return RobotStatus.OFFLINE; // Default if no status provided
        }

        try {
            String statusStr = json.get("status").asText().toUpperCase();
            return RobotStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown robot status received: {}, defaulting to OFFLINE", json.get("status").asText());
            return RobotStatus.OFFLINE;
        }
    }


    private String extractRobotIdFromTopic(String topic) {
        try {
            String[] parts = topic.split("/");
            return parts.length >= 3 ? parts[2] : null;
        } catch (Exception e) {
            log.error("Error extracting robot ID from topic: {}", topic, e);
            return null;
        }
    }
}