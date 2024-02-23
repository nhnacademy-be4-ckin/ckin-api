package store.ckin.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * KeyManager 클래스에서 사용되는 요소들을 관리하는 Properties 클래스
 *
 * @author 김준현
 * @version 2024. 02. 19
 */

@Getter
@Setter
@ConfigurationProperties("ckin.keymanager")
public class KeyManagerProperties {
    private String appKey;
    private String password;
    private String url;
    private String path;
}
