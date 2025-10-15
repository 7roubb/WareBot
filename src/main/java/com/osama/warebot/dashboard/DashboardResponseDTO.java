package com.osama.warebot.dashboard;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponseDTO {
    private long totalProducts;
    private long totalShelves;
    private long totalRobots;
    private long availableRobots;
}
