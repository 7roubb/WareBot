package com.osama.warebot.robots;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RobotRepository extends MongoRepository<Robot, String> {

    Page<Robot> findByDeletedFalse(Pageable pageable);

    Optional<Robot> findByIdAndDeletedFalse(String id);

    Optional<Robot> findByNameIgnoreCaseAndDeletedFalse(String name);

    Optional<Robot> findById(String id);

    long countByDeletedFalse();

    long countByDeletedFalseAndStatus(RobotStatus status);

    Page<Robot> findByStatusAndDeletedFalse(RobotStatus status, Pageable pageable);

    Page<Robot> findByAvailableAndDeletedFalse(boolean available, Pageable pageable);

    @Query("{ 'currentShelfId': ?0, 'deleted': false }")
    Optional<Robot> findByCurrentShelfIdAndDeletedFalse(String shelfId);
}
