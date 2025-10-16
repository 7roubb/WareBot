package com.osama.warebot.shelves;

import com.osama.warebot.common.OnCreate;
import com.osama.warebot.common.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Validated
public class ShelfRequestDto {

    @NotBlank(groups = OnUpdate.class, message = "{shelf.id.required}")
    private String id;

    @NotBlank(groups = OnCreate.class, message = "{warehouse.id.required}")
    private String warehouseId;

    @NotNull(groups = OnCreate.class, message = "{shelf.xCoord.required}")
    @Min(value = 0, groups = {OnCreate.class, OnUpdate.class}, message = "{shelf.xCoord.min}")
    private int xCoord;

    @NotNull(groups = OnCreate.class, message = "{shelf.yCoord.required}")
    @Min(value = 0, groups = {OnCreate.class, OnUpdate.class}, message = "{shelf.yCoord.min}")
    private int yCoord;

    @Min(value = 0, groups = {OnCreate.class, OnUpdate.class}, message = "{shelf.level.min}")
    private Integer level;

    @NotBlank(groups = OnCreate.class, message = "{shelf.status.required}")
    private String status;  // e.g., "available", "in-use", "maintenance"

    private Boolean available;


    private List<@NotBlank(message = "{product.id.notblank}") String> productIds;
}
