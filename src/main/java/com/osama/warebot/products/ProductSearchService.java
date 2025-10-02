package com.osama.warebot.products;

import com.osama.warebot.exceptions.CustomExceptions;
import com.osama.warebot.shelves.Shelf;
import com.osama.warebot.shelves.ShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ShelfRepository shelfRepository;

    public List<ProductIndex> searchByName(String name) {
        return productSearchRepository.findByNameContaining(name);
    }

    public List<ProductIndex> searchByTag(String tag) {
        return productSearchRepository.findByTagsContaining(tag);
    }

    public List<ProductIndex> searchByCategory(String category) {
        return productSearchRepository.findByCategory(category);
    }

    public void indexProduct(ProductIndex productIndex) {
        productSearchRepository.save(productIndex);
    }

    public List<ProductIndex> searchProductsInShelf(String shelfId, String keyword) {
        Shelf shelf = shelfRepository.findByIdAndDeletedFalse(shelfId)
                .orElseThrow(() -> new CustomExceptions.ShelfNotFoundException(shelfId));

        List<String> productIds = shelf.getProductIds();

        return productSearchRepository.findByNameContaining(keyword)
                .stream()
                .filter(p -> productIds.contains(p.getId()))
                .toList();
    }
}
