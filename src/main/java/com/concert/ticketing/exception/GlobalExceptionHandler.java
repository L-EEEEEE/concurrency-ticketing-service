package com.concert.ticketing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이미 예약된 좌석입니다.
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalArgumentException e) {
        Map<String, String> response = new HashMap<>();
        response.put("code", "CONFLICT");
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // 사용자를 찾을 수 없습니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e){
        Map<String, String> response = new HashMap<>();
        response.put("code", "BAD_REQUEST");
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 락 획득 실패
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException e) {
        Map<String, String> response = new HashMap<>();
        response.put("code", "ERROR");
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
