package com.poly.backend.config;

import com.poly.backend.dto.ErrorDTO;
import com.poly.backend.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
/**
 * Lớp RestExceptionHandler xử lý các ngoại lệ trong toàn bộ ứng dụng.
 * Nó bắt các ngoại lệ AppException và trả về phản hồi phù hợp với thông tin lỗi.
 */
public class RestExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    @ResponseBody
    /**
     * Phương thức xử lý ngoại lệ AppException
     * @param ex Ngoại lệ AppException
     * @return Đối tượng ResponseEntity chứa thông tin lỗi
     */
    public ResponseEntity<ErrorDTO> handleException(AppException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDTO.builder().message(ex.getMessage()).build());
    }
}
