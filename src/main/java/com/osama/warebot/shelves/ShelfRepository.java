package com.osama.warebot.shelves;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelfRepository extends MongoRepository<Shelf, String> {

    Page<Shelf> findByDeletedFalse(Pageable pageable);

    Optional<Shelf> findByIdAndDeletedFalse(String id);

    Optional<Shelf> findByWarehouseIdAndXCoordAndYCoordAndLevelAndDeletedFalse(
            String warehouseId, int xCoord, int yCoord, int level
    );

    Optional<Shelf> findById(String id);
    long countByDeletedFalse();

}
