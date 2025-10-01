package com.osama.warebot.robots;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(value = "robot")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Robot {

    @Id
    private String id;

    private String name;

    private boolean available; // true if robot can be assigned

    private String status; // "idle", "busy", "maintenance"

    private String currentShelfId; // shelf robot is currently assigned to (optional)

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private boolean deleted; // soft delete
}
