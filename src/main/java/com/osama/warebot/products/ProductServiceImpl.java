package com.osama.warebot.products;


import com.osama.warebot.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageStorageService imageStorageService;

    @Override
    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return productRepository.findByDeletedFalse(pageable)
                .map(ProductMapper::toResponseDto);
    }

    @Override
    public ProductResponseDto getProductByID(String id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .map(ProductMapper::toResponseDto)
                .orElseThrow(() -> new CustomExceptions.ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public Boolean saveProduct(ProductRequestDto productDto) {
        productRepository.findByNameIgnoreCaseAndDeletedFalse(productDto.getName())
                .ifPresent(p -> {
                    throw new CustomExceptions.ProductAlreadyExistsException(productDto.getName());
                });

        List<String> imageUrls = processImages(productDto.getImageBase64List());

        Product product = ProductMapper.toProduct(productDto);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        product.setDeleted(false);
        product.setImageUrls(imageUrls);

        productRepository.save(product);

        log.info("Product saved successfully: {}", product.getName());
        return true;
    }

    @Override
    @Transactional
    public Boolean updateProduct(ProductRequestDto productDto) {
        log.debug("Attempting to update product with ID: {}", productDto.getId());
        return productRepository.findByIdAndDeletedFalse(productDto.getId())
                .map(existingProduct -> {
                    Optional.ofNullable(productDto.getName()).ifPresent(existingProduct::setName);
                    Optional.ofNullable(productDto.getDescriptions()).ifPresent(existingProduct::setDescriptions);
                    Optional.of(productDto.getPrice()).ifPresent(existingProduct::setPrice);
                    Optional.ofNullable(productDto.getCategory()).ifPresent(existingProduct::setCategory);
                    Optional.ofNullable(productDto.getTags()).ifPresent(existingProduct::setTags);
                    Optional.of(productDto.getQuantityInStock()).ifPresent(existingProduct::setQuantityInStock);
                    existingProduct.setAvailable(productDto.isAvailable());
                    Optional.ofNullable(productDto.getLocalizedNames()).ifPresent(existingProduct::setLocalizedNames);
                    Optional.of(productDto.getDiscountPercentage()).ifPresent(existingProduct::setDiscountPercentage);
                    existingProduct.setOnSale(productDto.getOnSale());
                    Optional.ofNullable(productDto.getSaleStart()).ifPresent(existingProduct::setSaleStart);
                    Optional.ofNullable(productDto.getSaleEnd()).ifPresent(existingProduct::setSaleEnd);

                    if (productDto.getImageBase64List() != null && !productDto.getImageBase64List().isEmpty()) {
                        List<String> newImageUrls = processImages(productDto.getImageBase64List());
                        existingProduct.setImageUrls(newImageUrls);
                    }

                    existingProduct.setUpdatedAt(Instant.now());
                    productRepository.save(existingProduct);
                    return true;
                })
                .orElseThrow(() -> {
                    log.error("Cannot update. Product not found with ID: {}", productDto.getId());
                    return new CustomExceptions.ProductNotFoundException(productDto.getId());
                });
    }

    @Override
    @Transactional
    public Boolean deleteProduct(String id) {
        log.debug("Attempting to delete product with ID: {}", id);
        return productRepository.findByIdAndDeletedFalse(id)
                .map(product -> {
                    product.setDeleted(true);
                    product.setUpdatedAt(Instant.now());
                    productRepository.save(product);
                    log.warn("Product soft-deleted: {}", product.getName());
                    return true;
                })
                .orElseThrow(() -> {
                    log.error("Cannot delete. Product not found with ID: {}", id);
                    return new CustomExceptions.ProductNotFoundException(id);
                });
    }

    private List<String> processImages(List<String> imageBase64List) {
        return imageBase64List.stream()
                .map(this::uploadImageFromBase64)
                .collect(Collectors.toList());
    }

    private String uploadImageFromBase64(String base64Data) {
        try {
            String[] parts = base64Data.split(",");
            String metaPart = parts[0];
            String dataPart = parts[1];
            String contentType = metaPart.split(":")[1].split(";")[0];

            String extension = switch (contentType) {
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                case "image/gif" -> ".gif";
                case "image/webp" -> ".webp";
                default ->
                        throw new CustomExceptions.ImageProcessingException("Unsupported image type: " + contentType);
            };

            String fileName = "product_" + UUID.randomUUID() + extension;
            byte[] imageBytes = Base64.getDecoder().decode(dataPart);

            imageStorageService.uploadImage(fileName, imageBytes, contentType);
            return imageStorageService.getImageUrl(fileName).toString();
        } catch (Exception e) {
            log.error("Image processing failed: {}", e.getMessage());
            throw new CustomExceptions.ImageProcessingException("Invalid image data: " + e.getMessage());
        }
    }
}