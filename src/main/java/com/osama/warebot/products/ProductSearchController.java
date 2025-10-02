package com.osama.warebot.products;

import com.osama.warebot.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search/products")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/name")
    public ApiResponse<List<ProductIndex>> searchByName(@RequestParam String q) {
        return ApiResponse.success(productSearchService.searchByName(q), HttpStatus.OK, "search.success");
    }

    @GetMapping("/tag")
    public ApiResponse<List<ProductIndex>> searchByTag(@RequestParam String tag) {
        return ApiResponse.success(productSearchService.searchByTag(tag), HttpStatus.OK, "search.success");
    }

    @GetMapping("/category")
    public ApiResponse<List<ProductIndex>> searchByCategory(@RequestParam String category) {
        return ApiResponse.success(productSearchService.searchByCategory(category), HttpStatus.OK, "search.success");
    }

}
