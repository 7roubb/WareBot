package com.osama.warebot.shelves;

import com.osama.warebot.products.Product;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(value = "shelf")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Shelf {

    @Id
    private String id;

    private String warehouseId;

    private int xCoord;
    private int yCoord;
    private int level;

    private boolean available;
    private String status;

    private List<String> productIds;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private boolean deleted;
}
