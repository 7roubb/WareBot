package com.osama.warebot.robots;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "robot_telemetry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobotTelemetry {
    @Id
    private String id;
    private String robotId;
    private double speed;
    private double batteryLife;
    private boolean charging;
    private double cpuUsage;
    private double temperature;
    private Instant timestamp;

    // Additional metrics you might want
    private double memoryUsage;
    private String networkStatus;
    private String location;
    private String currentTask;
}