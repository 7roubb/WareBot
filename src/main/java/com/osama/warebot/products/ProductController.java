package com.osama.warebot.products;


import com.osama.warebot.common.ApiResponse;
import com.osama.warebot.common.OnCreate;
import com.osama.warebot.common.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final MessageSource messageSource;

    @GetMapping
    public ApiResponse<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = productService.findAll(pageable);
        return ApiResponse.success(products, HttpStatus.OK, getMessage("product.get.all.success"));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable String id) {
        ProductResponseDto product = productService.getProductByID(id);
        return ApiResponse.success(product, HttpStatus.OK, getMessage("product.get.success", id));
    }
    @PostMapping
    public ApiResponse<Boolean> saveProduct(
            @Validated(OnCreate.class) @RequestBody ProductRequestDto productDto) {
        Boolean result = productService.saveProduct(productDto);
        return ApiResponse.success(result, HttpStatus.CREATED, getMessage("product.create.success", productDto.getName()));
    }
    @PutMapping
    public ApiResponse<Boolean> updateProduct(@Validated(OnUpdate.class) @RequestBody ProductRequestDto productDto) {
        Boolean result = productService.updateProduct(productDto);
        return ApiResponse.success(result, HttpStatus.OK, getMessage("product.update.success", productDto.getId()));
    }

    @DeleteMapping
    public ApiResponse<Boolean> deleteProduct(@RequestParam String id) {
        Boolean result = productService.deleteProduct(id);
        return ApiResponse.success(result, HttpStatus.OK, getMessage("product.delete.success", id));
    }

    private String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
