package com.osama.warebot.products;


import com.osama.warebot.common.OnCreate;
import com.osama.warebot.common.OnUpdate;
import com.osama.warebot.validation.ValidLocalizedEntries;
import com.osama.warebot.validation.ValidProductDescription;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
@Builder
public class ProductRequestDto {

    @NotBlank(groups = OnUpdate.class, message = "{user.id.notnull}")
    private String id;

    @NotBlank(groups = OnCreate.class, message = "{product.name.required}")
    @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 100, message = "{name.size.message}")
    private String name;

    // Changed to set of attribute maps with validation
    @NotEmpty(groups = OnCreate.class, message = "{product.descriptions.required}")
    @ValidProductDescription(groups = {OnCreate.class, OnUpdate.class})
    private Set<Map<String, Map<String, String>>> descriptions;

    @NotNull(groups = OnCreate.class, message = "{price.is.required}")
    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "{price.must.positive}")
    @DecimalMin(value = "0.01", groups = {OnCreate.class, OnUpdate.class}, message = "{price.min.size}")
    private double price;

    @NotNull(groups = OnCreate.class, message = "{category.is.required}")
    private Category category;

    @NotEmpty(groups = OnCreate.class, message = "{tag.message.empty}")
    private List<String> tags;

    @NotNull(groups = OnCreate.class, message = "{quantity.is.required}")
    @Min(value = 0, groups = {OnCreate.class, OnUpdate.class}, message = "{quantity.must.positive}")
    @Max(value = 10000, groups = {OnCreate.class, OnUpdate.class}, message = "{stock.exceed.message}")
    private int quantityInStock;

    private boolean available;

    @ValidLocalizedEntries(groups = {OnCreate.class, OnUpdate.class})
    private Map<String, String> localizedNames;

    @NotEmpty(groups = OnCreate.class, message = "{image.base64.required}")
    @Schema(description = "List of Base64 encoded images")
    private List<@NotBlank(message = "{image.base64.notblank}") String> imageBase64List;

    @DecimalMin(value = "0.0", groups = {OnCreate.class, OnUpdate.class}, message = "{discount.cannot.be.negative}")
    @DecimalMax(value = "100.0", groups = {OnCreate.class, OnUpdate.class}, message = "{discount.cannot.exceed}")
    private Double discountPercentage;

    private Boolean onSale;

    @FutureOrPresent(groups = OnCreate.class, message = "{sale.start.message.error}")
    private Instant saleStart;

    private Instant saleEnd;

    @AssertTrue(groups = OnCreate.class, message = "{sale.end.message.error}")
    private boolean isValidSalePeriod() {
        if (onSale != null && onSale) {
            return saleStart != null &&
                    saleEnd != null &&
                    saleEnd.isAfter(saleStart);
        }
        return true;
    }

    @AssertTrue(groups = OnCreate.class, message = "{discount.requires.active.sale}")
    private boolean isValidDiscount() {
        if (discountPercentage != null && discountPercentage > 0) {
            return onSale != null && onSale;
        }
        return true;
    }
}