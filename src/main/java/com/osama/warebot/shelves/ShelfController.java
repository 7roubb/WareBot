package com.osama.warebot.shelves;

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
@RequestMapping("/shelves")
@RequiredArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;
    private final MessageSource messageSource;

    @GetMapping
    public ApiResponse<Page<ShelfResponseDto>> getAllShelves(Pageable pageable) {
        Page<ShelfResponseDto> shelves = shelfService.findAll(pageable);
        return ApiResponse.success(shelves, HttpStatus.OK, getMessage("shelf.get.all.success"));
    }

    @GetMapping("/{id}")
    public ApiResponse<ShelfResponseDto> getShelfById(@PathVariable String id) {
        ShelfResponseDto shelf = shelfService.getShelfById(id);
        return ApiResponse.success(shelf, HttpStatus.OK, getMessage("shelf.get.success", id));
    }

    @PostMapping
    public ApiResponse<Boolean> saveShelf(
            @Validated(OnCreate.class) @RequestBody ShelfRequestDto shelfRequestDto) {
        Boolean result = shelfService.saveShelf(shelfRequestDto);
        return ApiResponse.success(result, HttpStatus.CREATED, getMessage("shelf.create.success"));
    }

    @PutMapping
    public ApiResponse<Boolean> updateShelf(
            @Validated(OnUpdate.class) @RequestBody ShelfRequestDto shelfRequestDto) {
        Boolean result = shelfService.updateShelf(shelfRequestDto);
        return ApiResponse.success(result, HttpStatus.OK, getMessage("shelf.update.success", shelfRequestDto.getId()));
    }

    @DeleteMapping
    public ApiResponse<Boolean> deleteShelf(@RequestParam String id) {
        Boolean result = shelfService.deleteShelf(id);
        return ApiResponse.success(result, HttpStatus.OK, getMessage("shelf.delete.success", id));
    }

    private String getMessage(String code, String... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
