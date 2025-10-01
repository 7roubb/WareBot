package com.osama.warebot.robots;

import java.util.Optional;

public class RobotMapper {

    public static RobotResponseDto toResponseDto(Robot robot) {
        return Optional.ofNullable(robot)
                .map(r -> RobotResponseDto.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .available(r.isAvailable())
                        .status(r.getStatus())
                        .currentShelfId(r.getCurrentShelfId())
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .orElse(null);
    }

    public static Robot toRobot(RobotRequestDto requestDto) {
        return Optional.ofNullable(requestDto)
                .map(r -> Robot.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .available(r.getAvailable() != null ? r.getAvailable() : true)
                        .status(r.getStatus())
                        .currentShelfId(r.getCurrentShelfId())
                        .deleted(false)
                        .build())
                .orElse(null);
    }
}
