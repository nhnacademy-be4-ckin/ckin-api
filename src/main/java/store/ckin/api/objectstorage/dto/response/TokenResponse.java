package store.ckin.api.objectstorage.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 03. 04.
 */
@Getter
public class TokenResponse {
    private Access access;

    @Getter
    @NoArgsConstructor
    public static class Access {
        Token token;
    }

    @Getter
    @NoArgsConstructor
    public static class Token {
        LocalDateTime expires;
        String id;
    }
}
