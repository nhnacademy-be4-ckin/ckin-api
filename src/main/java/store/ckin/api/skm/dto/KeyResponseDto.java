package store.ckin.api.skm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * NHN SKM API 로부터 오는 응답에 대한 DTO 클래스
 *
 * @author 김준현
 * @version 2024. 02. 19
 */
@Getter
public class KeyResponseDto {
    private Header header;
    private Body body;

    @Getter
    @NoArgsConstructor
    public static class Body {
        private String secret;
    }

    @Getter
    @NoArgsConstructor
    public static class Header {
        private Integer resultCode;
        private String resultMessage;
        private boolean isSuccessful;
    }
}
