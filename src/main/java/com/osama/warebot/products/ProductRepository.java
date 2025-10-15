package com.osama.warebot.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndDeletedFalse(String id);

    Optional<Product> findByNameIgnoreCaseAndDeletedFalse(String name);

    Optional<Product> findById(String id);

    long countByDeletedFalse();

}
