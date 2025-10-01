package com.osama.warebot.shelves;

import com.osama.warebot.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;

    @Override
    public Page<ShelfResponseDto> findAll(Pageable pageable) {
        return shelfRepository.findByDeletedFalse(pageable)
                .map(ShelfMapper::toResponseDto);
    }

    @Override
    public ShelfResponseDto getShelfById(String id) {
        return shelfRepository.findByIdAndDeletedFalse(id)
                .map(ShelfMapper::toResponseDto)
                .orElseThrow(() -> new CustomExceptions.ShelfNotFoundException(id));
    }

    @Override
    @Transactional
    public Boolean saveShelf(ShelfRequestDto shelfRequestDto) {
        // Optional: check if a shelf already exists at the same coordinates
        shelfRepository.findByWarehouseIdAndXCoordAndYCoordAndLevelAndDeletedFalse(
                shelfRequestDto.getWarehouseId(),
                shelfRequestDto.getXCoord(),
                shelfRequestDto.getYCoord(),
                shelfRequestDto.getLevel() != null ? shelfRequestDto.getLevel() : 0
        ).ifPresent(s -> {
            throw new CustomExceptions.ShelfAlreadyExistsException(
                    "Shelf already exists at the specified coordinates"
            );
        });

        Shelf shelf = ShelfMapper.toShelf(shelfRequestDto);
        shelf.setCreatedAt(Instant.now());
        shelf.setUpdatedAt(Instant.now());
        shelf.setDeleted(false);

        shelfRepository.save(shelf);
        log.info("Shelf saved successfully at coordinates ({}, {}, {})",
                shelf.getXCoord(), shelf.getYCoord(), shelf.getLevel());
        return true;
    }

    @Override
    @Transactional
    public Boolean updateShelf(ShelfRequestDto shelfRequestDto) {
        log.debug("Attempting to update shelf with ID: {}", shelfRequestDto.getId());
        return shelfRepository.findByIdAndDeletedFalse(shelfRequestDto.getId())
                .map(existingShelf -> {
                    Optional.ofNullable(shelfRequestDto.getWarehouseId()).ifPresent(existingShelf::setWarehouseId);
                    Optional.ofNullable(shelfRequestDto.getXCoord()).ifPresent(existingShelf::setXCoord);
                    Optional.ofNullable(shelfRequestDto.getYCoord()).ifPresent(existingShelf::setYCoord);
                    Optional.ofNullable(shelfRequestDto.getLevel()).ifPresent(existingShelf::setLevel);
                    Optional.ofNullable(shelfRequestDto.getStatus()).ifPresent(existingShelf::setStatus);
                    Optional.ofNullable(shelfRequestDto.getAvailable()).ifPresent(existingShelf::setAvailable);
                    Optional.ofNullable(shelfRequestDto.getProductIds()).ifPresent(existingShelf::setProductIds);

                    existingShelf.setUpdatedAt(Instant.now());
                    shelfRepository.save(existingShelf);
                    log.info("Shelf updated successfully: {}", existingShelf.getId());
                    return true;
                })
                .orElseThrow(() -> {
                    log.error("Cannot update. Shelf not found with ID: {}", shelfRequestDto.getId());
                    return new CustomExceptions.ShelfNotFoundException(shelfRequestDto.getId());
                });
    }

    @Override
    @Transactional
    public Boolean deleteShelf(String id) {
        log.debug("Attempting to delete shelf with ID: {}", id);
        return shelfRepository.findByIdAndDeletedFalse(id)
                .map(shelf -> {
                    shelf.setDeleted(true);
                    shelf.setUpdatedAt(Instant.now());
                    shelfRepository.save(shelf);
                    log.warn("Shelf soft-deleted: {}", shelf.getId());
                    return true;
                })
                .orElseThrow(() -> {
                    log.error("Cannot delete. Shelf not found with ID: {}", id);
                    return new CustomExceptions.ShelfNotFoundException(id);
                });
    }
}
