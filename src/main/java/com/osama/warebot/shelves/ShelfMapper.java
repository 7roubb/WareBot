package com.osama.warebot.shelves;

import java.util.Optional;

public class ShelfMapper {

    public static ShelfResponseDto toResponseDto(Shelf shelf) {
        return Optional.ofNullable(shelf)
                .map(s -> ShelfResponseDto.builder()
                        .id(s.getId())
                        .warehouseId(s.getWarehouseId())
                        .xCoord(s.getXCoord())
                        .yCoord(s.getYCoord())
                        .level(s.getLevel())
                        .available(s.isAvailable())
                        .status(s.getStatus())
                        .productIds(s.getProductIds())
                        .createdAt(s.getCreatedAt())
                        .updatedAt(s.getUpdatedAt())
                        .build()
                ).orElse(null);
    }

    public static Shelf toShelf(ShelfRequestDto requestDto) {
        return Optional.ofNullable(requestDto)
                .map(r -> Shelf.builder()
                        .id(r.getId())
                        .warehouseId(r.getWarehouseId())
                        .xCoord(r.getXCoord())
                        .yCoord(r.getYCoord())
                        .level(r.getLevel())
                        .available(r.getAvailable() != null ? r.getAvailable() : true)
                        .status(r.getStatus())
                        .productIds(r.getProductIds())
                        .deleted(false)
                        .build()
                ).orElse(null);
    }
}
