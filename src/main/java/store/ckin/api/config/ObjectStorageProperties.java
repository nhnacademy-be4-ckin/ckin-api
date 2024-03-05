package store.ckin.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * object storage properties 설정.
 *
 * @author 나국로
 * @version 2024. 02. 29.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "storage")
@RequiredArgsConstructor
public class ObjectStorageProperties {

    private String url;
    private String username;
    private String containerName;
    private String tenantId;
    private String password;
    private String identity;
}
