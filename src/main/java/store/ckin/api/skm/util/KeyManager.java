package store.ckin.api.skm.util;

import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import store.ckin.api.config.KeyManagerProperties;
import store.ckin.api.skm.dto.KeyResponseDto;
import store.ckin.api.skm.exception.KeyMangerException;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Objects;

/**
 * NHN Cloud SKM 에서 저장된 데이터를 가져오기 위한 매니저 클래스
 *
 * @author 김준현
 * @version 2024. 02. 19
 */

@Component
@RequiredArgsConstructor
public class KeyManager {
    private final KeyManagerProperties properties;

    /**
     * /resources/ckin.p12 인증서를 사용하여 클라우드에서 기밀 데이터를 가져오는 메서드
     *
     * @param keyId 조회를 원하는 데이터의 Key value
     * @return key value에 해당하는 데이터
     */
    public String keyStore(String keyId) {
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            InputStream result = new ClassPathResource("ckin.p12").getInputStream();
            clientStore.load(result, properties.getPassword().toCharArray());

            SSLContext sslContext = SSLContextBuilder.create()
                    .setProtocol("TLS")
                    .loadKeyMaterial(clientStore, properties.getPassword().toCharArray())
                    .loadTrustMaterial(new TrustSelfSignedStrategy())
                    .build();

            SSLConnectionSocketFactory sslConnectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory(httpClient);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            RestTemplate restTemplate = new RestTemplate(requestFactory);

            URI uri = UriComponentsBuilder
                    .fromUriString(properties.getUrl())
                    .path(properties.getPath())
                    .encode()
                    .build()
                    .expand(properties.getAppKey(), keyId)
                    .toUri();
            return Objects.requireNonNull(restTemplate.exchange(uri,
                                    HttpMethod.GET,
                                    new HttpEntity<>(headers),
                                    KeyResponseDto.class)
                            .getBody())
                    .getBody()
                    .getSecret();
        } catch (KeyStoreException | IOException | CertificateException
                 | NoSuchAlgorithmException
                 | UnrecoverableKeyException
                 | KeyManagementException e) {
            throw new KeyMangerException(e.getMessage());
        }
    }
}
