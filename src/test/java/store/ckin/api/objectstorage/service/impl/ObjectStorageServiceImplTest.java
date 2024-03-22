package store.ckin.api.objectstorage.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.config.ObjectStorageProperties;
import store.ckin.api.file.repository.FileRepository;
import store.ckin.api.objectstorage.dto.response.TokenResponse;
import store.ckin.api.skm.util.KeyManager;

/**
 * ObjectStorageServiceImplTest.
 *
 * @author 나국로
 * @version 2024. 03. 21.
 */
@ExtendWith(MockitoExtension.class)
class ObjectStorageServiceImplTest {
    @Mock
    private MultipartFile mockMultipartFile;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private KeyManager keyManager;

    @Mock
    private ObjectStorageProperties properties;
    @Mock
    private ResponseEntity<TokenResponse> responseEntity;

    @InjectMocks
    private ObjectStorageServiceImpl objectStorageService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(objectStorageService, "tokenId", "testTokenId");
        ReflectionTestUtils.setField(objectStorageService, "expires", LocalDateTime.now().plusHours(1));
    }

    @Test
    @DisplayName("TokenResponse 객체 생성 테스트")
    void createTokenResponse() {
        // 테스트에 사용할 데이터
        LocalDateTime expires = LocalDateTime.of(2024, 3, 4, 12, 0);
        String tokenId = "testTokenId";

        // TokenResponse 객체 생성
        TokenResponse.Token token = new TokenResponse.Token();
        ReflectionTestUtils.setField(token, "expires", expires);
        ReflectionTestUtils.setField(token, "id", tokenId);


        TokenResponse.Access access = new TokenResponse.Access();
        ReflectionTestUtils.setField(access, "token", token);

        TokenResponse tokenResponse = new TokenResponse();
        ReflectionTestUtils.setField(tokenResponse, "access", access);

        // 생성된 객체의 필드 값 확인
        assertEquals(expires, tokenResponse.getAccess().getToken().getExpires());
        assertEquals(tokenId, tokenResponse.getAccess().getToken().getId());
    }


}
