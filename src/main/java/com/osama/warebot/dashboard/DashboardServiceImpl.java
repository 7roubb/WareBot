package com.osama.warebot.dashboard;

import com.osama.warebot.products.ProductRepository;
import com.osama.warebot.robots.RobotRepository;
import com.osama.warebot.robots.RobotStatus;
import com.osama.warebot.shelves.ShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final ShelfRepository shelfRepository;
    private final RobotRepository robotRepository;

    @Override
    public DashboardResponseDTO getDashboardStats() {
        long totalProducts = productRepository.countByDeletedFalse();
        long totalShelves = shelfRepository.countByDeletedFalse();
        long totalRobots = robotRepository.countByDeletedFalse();
        long availableRobots = robotRepository.countByDeletedFalseAndStatus(RobotStatus.IDLE);

        log.info("Dashboard stats -> Products: {}, Shelves: {}, Robots: {}, Available Robots: {}",
                totalProducts, totalShelves, totalRobots, availableRobots);

        return DashboardResponseDTO.builder()
                .totalProducts(totalProducts)
                .totalShelves(totalShelves)
                .totalRobots(totalRobots)
                .availableRobots(availableRobots)
                .build();
    }
}