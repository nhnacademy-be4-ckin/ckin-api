package store.ckin.api.eureka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 02. 17.
 */
@RestController
public class TestController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/api/port")
    public String getPort() {
        return port + "번 포트로 서비스중입니다.";
    }

}