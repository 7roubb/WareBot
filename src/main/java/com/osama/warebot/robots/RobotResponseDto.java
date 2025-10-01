package com.osama.warebot.robots;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RobotResponseDto {

    private String id;
    private String name;
    private boolean available;
    private RobotStatus status;
    private String currentShelfId;
    private Instant createdAt;
    private Instant updatedAt;
}
