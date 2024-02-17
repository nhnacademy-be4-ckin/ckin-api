package store.ckin.api.eureka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Eureka 서버 테스트용 컨트롤러 클래스입니다.
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
