package com.osama.warebot.products;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductIndex, String> {

    List<ProductIndex> findByNameContaining(String name);

    List<ProductIndex> findByTagsContaining(String tag);

    List<ProductIndex> findByCategory(String category);
}
