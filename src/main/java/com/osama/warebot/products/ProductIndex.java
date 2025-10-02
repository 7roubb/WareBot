package com.osama.warebot.products;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Document(indexName = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Object)
    private Set<Map<String, Map<String, String>>> descriptions;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Boolean)
    private boolean available;

    @Field(type = FieldType.Double)
    private double averageRating;

    @Field(type = FieldType.Keyword)
    private Map<String, String> localizedNames;

    @Field(type = FieldType.Keyword)
    private List<String> imageUrls;

    @Field(type = FieldType.Boolean)
    private boolean onSale;

    @Field(type = FieldType.Date)
    private Instant createdAt;
}
