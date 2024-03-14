package store.ckin.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.ckin.api.skm.util.KeyManager;

import javax.sql.DataSource;

/**
 * DBCP2 설정을 위한 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 16.
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final DbProperties dbProperties;
    private final KeyManager keyManager;

    @Bean
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName(keyManager.keyStore(dbProperties.getDriver()));
        basicDataSource.setUrl(keyManager.keyStore(dbProperties.getUrl()));
        basicDataSource.setUsername(keyManager.keyStore(dbProperties.getUsername()));
        basicDataSource.setPassword(keyManager.keyStore(dbProperties.getPassword()));

        basicDataSource.setInitialSize(dbProperties.getInitialSize());
        basicDataSource.setMaxTotal(dbProperties.getMaxTotal());
        basicDataSource.setMaxIdle(dbProperties.getMaxIdle());
        basicDataSource.setMinIdle(dbProperties.getMinIdle());

        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setValidationQuery("SELECT 1");

        basicDataSource.setMaxWaitMillis(dbProperties.getMaxWait());
        return basicDataSource;
    }

}