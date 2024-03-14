package store.ckin.api.objectstorage.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
