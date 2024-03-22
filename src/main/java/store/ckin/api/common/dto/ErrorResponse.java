package store.ckin.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception 발생 시 정보를 담아 JSON 형태로 응답하기 위한 클래스
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private final HttpStatus code;
    private final String message;
}
