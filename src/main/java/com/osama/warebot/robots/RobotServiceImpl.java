package com.osama.warebot.robots;

import com.osama.warebot.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;

    @Override
    public Page<RobotResponseDto> findAll(Pageable pageable) {
        return robotRepository.findByDeletedFalse(pageable)
                .map(RobotMapper::toResponseDto);
    }

    @Override
    public RobotResponseDto getRobotById(String id) {
        return robotRepository.findByIdAndDeletedFalse(id)
                .map(RobotMapper::toResponseDto)
                .orElseThrow(CustomExceptions.RobotNotFoundException::new);
    }

    @Override
    @Transactional
    public Boolean saveRobot(RobotRequestDto requestDto) {
        robotRepository.findByNameIgnoreCaseAndDeletedFalse(requestDto.getName())
                .ifPresent(r -> {
                    throw new CustomExceptions.RobotNotFoundException();
                });

        Robot robot = RobotMapper.toRobot(requestDto);
        robot.setCreatedAt(Instant.now());
        robot.setUpdatedAt(Instant.now());
        robot.setDeleted(false);
        robotRepository.save(robot);

        log.info("Robot saved successfully: {}", robot.getName());
        return true;
    }

    @Override
    @Transactional
    public Boolean updateRobot(RobotRequestDto requestDto) {
        return robotRepository.findByIdAndDeletedFalse(requestDto.getId())
                .map(robot -> {
                    Optional.ofNullable(requestDto.getName()).ifPresent(robot::setName);
                    Optional.ofNullable(requestDto.getAvailable()).ifPresent(robot::setAvailable);
                    Optional.ofNullable(requestDto.getStatus()).ifPresent(robot::setStatus);
                    Optional.ofNullable(requestDto.getCurrentShelfId()).ifPresent(robot::setCurrentShelfId);

                    robot.setUpdatedAt(Instant.now());
                    robotRepository.save(robot);
                    log.info("Robot updated successfully: {}", robot.getId());
                    return true;
                })
                .orElseThrow(CustomExceptions.RobotNotFoundException::new);
    }

    @Override
    @Transactional
    public Boolean deleteRobot(String id) {
        return robotRepository.findByIdAndDeletedFalse(id)
                .map(robot -> {
                    robot.setDeleted(true);
                    robot.setUpdatedAt(Instant.now());
                    robotRepository.save(robot);
                    log.warn("Robot soft-deleted: {}", robot.getId());
                    return true;
                })
                .orElseThrow(CustomExceptions.RobotNotFoundException::new);
    }
}
