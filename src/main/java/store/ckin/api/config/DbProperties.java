package store.ckin.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "ckin.mysql")
public class DbProperties {

    private String driver;

    private String url;

    private String username;

    private String password;

    private Integer initialSize;

    private Integer maxTotal;

    private Integer maxIdle;

    private Integer minIdle;

    private Long maxWait;

}
