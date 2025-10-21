package com.osama.warebot.robots;

import com.osama.warebot.common.ApiResponse;
import com.osama.warebot.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/robots")
@RequiredArgsConstructor
public class RobotTelemetryController {

    private final RobotService robotService;
    private final RobotTelemetryRepository telemetryRepository;
    private final MessageSource messageSource;

    @GetMapping("/{robotId}/telemetry/latest")
    public ApiResponse<RobotTelemetry> getLatestTelemetry(@PathVariable String robotId) {
        RobotTelemetry telemetry = telemetryRepository.findTop10ByRobotIdOrderByTimestampDesc(robotId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomExceptions.TelemetryNotFoundException());

        return ApiResponse.success(telemetry, HttpStatus.OK, getMessage("robot.telemetry.latest.success", robotId));
    }

    @GetMapping("/{robotId}/telemetry/history")
    public ApiResponse<List<RobotTelemetry>> getTelemetryHistory(
            @PathVariable String robotId,
            @RequestParam(defaultValue = "50") int limit) {

        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<RobotTelemetry> telemetry = telemetryRepository.findByRobotId(robotId, pageable);

        return ApiResponse.success(telemetry.getContent(), HttpStatus.OK,
                getMessage("robot.telemetry.history.success", robotId));
    }

    @PatchMapping("/{robotId}/status")
    public ApiResponse<Boolean> updateRobotStatus(
            @PathVariable String robotId,
            @RequestParam RobotStatus status) {

        // Create a request DTO for the update
        RobotRequestDto requestDto = RobotRequestDto.builder()
                .id(robotId)
                .status(status)
                .build();

        Boolean result = robotService.updateRobot(requestDto);
        return ApiResponse.success(result, HttpStatus.OK,
                getMessage("robot.status.update.success", robotId, status.toString()));
    }

    @PatchMapping("/{robotId}/assign")
    public ApiResponse<Boolean> assignRobotToShelf(
            @PathVariable String robotId,
            @RequestParam String shelfId) {

        RobotRequestDto requestDto = RobotRequestDto.builder()
                .id(robotId)
                .currentShelfId(shelfId)
                .status(RobotStatus.BUSY)
                .available(false)
                .build();

        Boolean result = robotService.updateRobot(requestDto);
        return ApiResponse.success(result, HttpStatus.OK,
                getMessage("robot.assign.success", robotId, shelfId));
    }

    @PatchMapping("/{robotId}/release")
    public ApiResponse<Boolean> releaseRobot(@PathVariable String robotId) {
        RobotRequestDto requestDto = RobotRequestDto.builder()
                .id(robotId)
                .currentShelfId(null)
                .status(RobotStatus.IDLE)
                .available(true)
                .build();

        Boolean result = robotService.updateRobot(requestDto);
        return ApiResponse.success(result, HttpStatus.OK,
                getMessage("robot.release.success", robotId));
    }

    @GetMapping("/status/{status}")
    public ApiResponse<Page<RobotResponseDto>> getRobotsByStatus(
            @PathVariable String status,
            Pageable pageable) {

        try {
            RobotStatus robotStatus = RobotStatus.valueOf(status.toUpperCase());
            Page<RobotResponseDto> robots = robotService.findByStatus(robotStatus, pageable);
            return ApiResponse.success(robots, HttpStatus.OK,
                    getMessage("robot.get.by.status.success", status));
        } catch (IllegalArgumentException e) {
            throw new CustomExceptions.InvalidStatusException();
        }
    }

    private String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}