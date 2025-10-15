package com.osama.warebot.dashboard;

import com.osama.warebot.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponseDTO> getDashboardStats() {
        return ApiResponse.success(dashboardService.getDashboardStats(), HttpStatus.OK,"Done");
    }
}
