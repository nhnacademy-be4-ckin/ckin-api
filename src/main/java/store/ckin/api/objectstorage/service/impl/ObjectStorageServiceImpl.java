package store.ckin.api.objectstorage.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.config.ObjectStorageProperties;
import store.ckin.api.file.entity.File;
import store.ckin.api.file.exception.FileNotFoundException;
import store.ckin.api.file.repository.FileRepository;
import store.ckin.api.objectstorage.dto.request.TokenRequest;
import store.ckin.api.objectstorage.dto.response.TokenResponse;
import store.ckin.api.objectstorage.service.ObjectStorageService;
import store.ckin.api.skm.util.KeyManager;

/**
 * ObjectStorageService 구현 클래스.
 *
 * @author 나국로
 * @version 2024. 03. 01.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ObjectStorageServiceImpl implements ObjectStorageService {

    private static final Map<String, String> FILE_EXTENSION_MAP = new HashMap<>();
    private String tokenId;

    private LocalDateTime expires;


    static {
        FILE_EXTENSION_MAP.put("jpeg", ".jpeg");
        FILE_EXTENSION_MAP.put("jpg", ".jpg");
        FILE_EXTENSION_MAP.put("png", ".png");
    }

    private final ObjectStorageProperties properties;
    private final RestTemplate restTemplate;
    private final FileRepository fileRepository;
    private final KeyManager keyManager;

    private static final String X_AUTH_TOKEN = "X-Auth-Token";

    public String requestToken() {

        // 헤더 생성
        String identityUrl = keyManager.keyStore(properties.getIdentity()) + "/tokens";

        if (Objects.isNull(tokenId)
                || expires.minusMinutes(1).isAfter(LocalDateTime.now())) {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            TokenRequest tokenRequest = new TokenRequest(keyManager.keyStore(properties.getTenantId()),
                    keyManager.keyStore(properties.getUsername()), keyManager.keyStore(properties.getPassword()));

            HttpEntity<TokenRequest> httpEntity
                    = new HttpEntity<>(tokenRequest, headers);

            // 토큰 요청
            ResponseEntity<TokenResponse> response
                    = this.restTemplate.exchange(identityUrl, HttpMethod.POST, httpEntity, TokenResponse.class);
            tokenId = Objects.requireNonNull(
                    response.getBody()).getAccess().getToken().getId();
            expires = Objects.requireNonNull(
                    response.getBody()).getAccess().getToken().getExpires();
        }

        return tokenId;
    }

    @Override
    public File saveFile(MultipartFile file, String category) throws IOException {

        String originalFileName = Objects.requireNonNull(file.getOriginalFilename(),
                "파일 이름이 null입니다.");

        // category 값 검증 (예: null이거나 공백이 아닌지 확인)
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리가 유효하지 않습니다.");
        }

        String fileExtension = getContentTypeFromFileName(originalFileName);

        // 확장자를 제외한 파일 이름 추출
        String fileNameWithoutExtension = originalFileName;
        int posImage = originalFileName.lastIndexOf(".");
        if (posImage > 0) {
            fileNameWithoutExtension = originalFileName.substring(0, posImage);
        }

        // UUID를 추가하여 저장될 파일 이름 생성
        String fileId = UUID.randomUUID() + "-" + fileNameWithoutExtension;


        String fileUrl = String.format("%s/%s/%s/%s%s",
                keyManager.keyStore(properties.getUrl()),
                keyManager.keyStore(properties.getContainerName()),
                category,
                fileId,
                fileExtension);

        InputStream inputStream = new ByteArrayInputStream(file.getBytes());

        // InputStream을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = request -> {
            request.getHeaders().add(X_AUTH_TOKEN, requestToken());
            IOUtils.copy(inputStream, request.getBody());
        };


        try {
            HttpMessageConverterExtractor<String> responseExtractor =
                    new HttpMessageConverterExtractor<>(String.class, restTemplate.getMessageConverters());

            restTemplate.execute(fileUrl, HttpMethod.PUT, requestCallback, responseExtractor);
            // 로그 기록
            log.info("파일 업로드 성공: {}", fileUrl);
            return File.builder()
                    .fileId(fileId)
                    .fileOriginName(originalFileName)
                    .fileUrl(fileUrl)
                    .fileCategory(category)
                    .fileExtension(fileExtension)
                    .build();
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", fileUrl, e);
            throw e;
        }
    }

    @Override
    public void deleteFile(String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN, requestToken());
        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        // REST API 호출을 통한 파일 삭제 요청
        this.restTemplate.exchange(url, HttpMethod.DELETE, requestHttpEntity, String.class);

        Optional<File> fileOptional = fileRepository.findByFileUrl(url);

        if (fileOptional.isPresent()) {
            fileRepository.delete(fileOptional.get());
        } else {
            throw new FileNotFoundException("File not found with URL: " + url);
        }
    }


    private String getContentTypeFromFileName(String fileName) {
        int posImage = fileName.lastIndexOf(".");
        if (posImage <= 0) {
            throw new IllegalArgumentException("유효하지 않은 파일 형식입니다.");
        }

        String fileExtension = fileName.substring(posImage + 1).toLowerCase();
        if (FILE_EXTENSION_MAP.containsKey(fileExtension)) {
            return FILE_EXTENSION_MAP.get(fileExtension);
        } else {
            throw new IllegalArgumentException("지원되지 않는 파일 확장자입니다.");
        }
    }


}
