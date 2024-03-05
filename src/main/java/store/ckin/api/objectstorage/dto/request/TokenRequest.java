package store.ckin.api.objectstorage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TokenRequest 클래스.
 *
 * @author 나국로
 * @version 2024. 03. 01.
 */
@Data
@NoArgsConstructor
public class TokenRequest {
    private Auth auth;

    @Getter
    @AllArgsConstructor
    public static class Auth {
        private String tenantId;
        private PasswordCredentials passwordCredentials;
    }

    @Getter
    @AllArgsConstructor
    public static class PasswordCredentials {
        private String username;
        private String password;
    }

    public TokenRequest(String tenantId, String username, String password) {
        this.auth = new Auth(tenantId, new PasswordCredentials(username, password));
    }
}