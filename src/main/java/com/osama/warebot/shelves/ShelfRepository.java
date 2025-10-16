package com.osama.warebot.shelves;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelfRepository extends MongoRepository<Shelf, String> {

    Page<Shelf> findByDeletedFalse(Pageable pageable);

    Optional<Shelf> findByIdAndDeletedFalse(String id);

    @Query("{ 'warehouseId': ?0, 'xCoord': ?1, 'yCoord': ?2, 'level': ?3, 'deleted': false }")
    Optional<Shelf> findExistingShelf(String warehouseId, Integer xCoord, Integer yCoord, Integer level);


    Optional<Shelf> findById(String id);
    long countByDeletedFalse();

}
