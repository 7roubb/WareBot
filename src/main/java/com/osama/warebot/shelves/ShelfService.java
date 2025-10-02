package com.osama.warebot.shelves;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelfService {

    Page<ShelfResponseDto> findAll(Pageable pageable);

    ShelfResponseDto getShelfById(String id);

    Boolean saveShelf(ShelfRequestDto shelfRequestDto);

    Boolean updateShelf(ShelfRequestDto shelfRequestDto);

    Boolean deleteShelf(String id);

    Boolean addProductToShelf(String shelfId, String productId);

    Boolean removeProductFromShelf(String shelfId, String productId);
}
