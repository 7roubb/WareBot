package com.osama.warebot.products;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    @Id
    private String id;

    private String name;
    private Set<Map<String, Map<String, String>>> descriptions;
    private double price;

    private Category category;
    private List<String> tags;

    private int quantityInStock;
    private boolean available;

    private double averageRating;
    private int numberOfReviews;

    private Map<String, String> localizedNames;

    private List<String> imageUrls;

    private double discountPercentage;
    private boolean onSale;
    private Instant saleStart;
    private Instant saleEnd;

    // Many-to-Many: Product can be stored in multiple shelves
    private List<String> shelfIds;  // Store Shelf IDs here

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private boolean deleted;
}
