package store.ckin.api.advice;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.ckin.api.advice.exception.GeneralAlreadyExistsException;
import store.ckin.api.advice.exception.GeneralBadRequestException;
import store.ckin.api.advice.exception.GeneralForbiddenException;
import store.ckin.api.advice.exception.GeneralNotFoundException;
import store.ckin.api.common.dto.ErrorResponse;

/**
 * Controller Advice 입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@RestControllerAdvice
public class WebRestControllerAdvice {

    /**
     * Validation 예외 처리 핸들러입니다.
     *
     * @param e MethodArgumentNotValidException
     * @return 400(BAD REQUEST), Error Message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * 전역으로 발생하는 Not Found Exception 처리 핸들러입니다.
     *
     * @param e xxxNotFoundException
     * @return 404(NOT FOUND), Error Message
     */
    @ExceptionHandler(GeneralNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(GeneralNotFoundException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 전역으로 발생하는 Already Exists Exception 처리 핸들러입니다.
     *
     * @param e xxxAlreadyExistsException
     * @return 409(CONFLICT), Error Message
     */
    @ExceptionHandler(GeneralAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(GeneralAlreadyExistsException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.CONFLICT)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * 전역으로 발생하는 BadRequest Exception 처리 핸들러입니다.
     *
     * @param e GeneralBadRequestException
     * @return 400(BAD REQUEST), Error Message
     */
    @ExceptionHandler(GeneralBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(GeneralBadRequestException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 전역으로 발생하는 Forbidden Exception 처리 핸들러입니다.
     *
     * @param e GeneralForbiddenException
     * @return 403(FORBIDDEN), Error Message
     */
    @ExceptionHandler(GeneralForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(GeneralForbiddenException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

}
