package com.osama.warebot.products;

import java.util.Optional;

public class ProductMapper {
    public static ProductResponseDto toResponseDto(Product product) {
        return Optional.ofNullable(product)
                .map(p -> ProductResponseDto.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .price(p.getPrice())
                        .category(p.getCategory())
                        .tags(p.getTags())
                        .quantityInStock(p.getQuantityInStock())
                        .available(p.isAvailable())
                        .averageRating(p.getAverageRating())
                        .numberOfReviews(p.getNumberOfReviews())
                        .localizedNames(p.getLocalizedNames())
                        .imageUrls(p.getImageUrls())
                        .discountPercentage(p.getDiscountPercentage())
                        .onSale(p.isOnSale())
                        .saleEnd(p.getSaleEnd())
                        .saleStart(p.getSaleStart())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .descriptions(product.getDescriptions())
                        .build()
                ).orElse(null);

    }
    public static Product toProduct(ProductRequestDto requestDto) {
        return Optional.ofNullable(requestDto)
                .map(r -> Product.builder()
                        .name(r.getName())
                        .descriptions(r.getDescriptions())
                        .price(r.getPrice())
                        .category(r.getCategory())
                        .tags(r.getTags())
                        .quantityInStock(r.getQuantityInStock())
                        .available(r.isAvailable())
                        .averageRating(0.0)
                        .numberOfReviews(0)
                        .localizedNames(r.getLocalizedNames())
                        .imageUrls(r.getImageBase64List())
                        .discountPercentage(
                                r.getDiscountPercentage() != null ? r.getDiscountPercentage() : 0.0
                        )
                        .onSale(
                                r.getOnSale() != null ? r.getOnSale() : false
                        )
                        .saleStart(r.getSaleStart())
                        .saleEnd(r.getSaleEnd())
                        .deleted(false)
                        .build())
                .orElse(null);
    }
}
