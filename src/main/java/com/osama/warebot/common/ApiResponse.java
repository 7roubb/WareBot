package com.osama.warebot.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Getter
@Setter
@Builder
public class ApiResponse<T> {

    private T content;
    private HttpStatus status;
    private String message;

    public static <T> ApiResponse<T> success(T content, HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .content(content)
                .status(status)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .content(null)
                .status(status)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> of(T content, HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .content(content)
                .status(status)
                .message(message)
                .build();
    }
    public static void writeSecurityErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        ApiResponse<Void> apiResponse = ApiResponse.error(message, status);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}