package com.osama.warebot.shelves;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShelfResponseDto {
    private String id;
    private String warehouseId;
    private int xCoord;
    private int yCoord;
    private int level;
    private boolean available;
    private String status;       // available, in-use, maintenance
    private List<String> productIds;  // product references
    private Instant createdAt;
    private Instant updatedAt;
}
