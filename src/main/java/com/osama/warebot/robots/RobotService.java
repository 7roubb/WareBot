package com.osama.warebot.robots;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RobotService {

    Page<RobotResponseDto> findAll(Pageable pageable);

    RobotResponseDto getRobotById(String id);

    Boolean saveRobot(RobotRequestDto robotRequestDto);

    Boolean updateRobot(RobotRequestDto robotRequestDto);

    Boolean deleteRobot(String id);
}
