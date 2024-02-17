package store.ckin.api.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * description
 *
 * @author 김준현
 * @version 2024. 02. 17
 */
@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
}
