package com.osama.warebot.robots;

import com.osama.warebot.common.ApiResponse;
import com.osama.warebot.common.OnCreate;
import com.osama.warebot.common.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/robots")
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;
    private final MessageSource messageSource;

    @GetMapping
    public ApiResponse<Page<RobotResponseDto>> getAllRobots(Pageable pageable) {
        Page<RobotResponseDto> robots = robotService.findAll(pageable);
        return ApiResponse.success(robots, HttpStatus.OK, getMessage("robot.get.all.success"));
    }

    @GetMapping("/{id}")
    public ApiResponse<RobotResponseDto> getRobotById(@PathVariable String id) {
        RobotResponseDto robot = robotService.getRobotById(id);
        return ApiResponse.success(robot, HttpStatus.OK, getMessage("robot.get.success", id));
    }

    @PostMapping
    public ApiResponse<Boolean> saveRobot(@Validated(OnCreate.class) @RequestBody RobotRequestDto requestDto) {
        Boolean result = robotService.saveRobot(requestDto);
        return ApiResponse.success(result, HttpStatus.CREATED, getMessage("robot.create.success"));
    }

    @PutMapping
    public ApiResponse<Boolean> updateRobot(@Validated(OnUpdate.class) @RequestBody RobotRequestDto requestDto) {
        Boolean result = robotService.updateRobot(requestDto);
        return ApiResponse.success(result, HttpStatus.OK, getMessage("robot.update.success", requestDto.getId()));
    }

    @DeleteMapping
    public ApiResponse<Boolean> deleteRobot(@RequestParam String id) {
        Boolean result = robotService.deleteRobot(id);
        return ApiResponse.success(result, HttpStatus.OK, getMessage("robot.delete.success", id));
    }

    private String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
