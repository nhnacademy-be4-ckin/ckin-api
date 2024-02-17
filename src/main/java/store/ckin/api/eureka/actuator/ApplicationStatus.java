package store.ckin.api.eureka.actuator;

import org.springframework.stereotype.Component;

/**
 * 어플리케이션 상태를 관리하는 클래스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 17.
 */

@Component
public class ApplicationStatus {

    private boolean status = true;

    public void stopService() {
        this.status = false;
    }

    public boolean getStatus() {
        return this.status;
    }
}
