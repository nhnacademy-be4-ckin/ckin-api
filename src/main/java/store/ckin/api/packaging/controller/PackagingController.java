package store.ckin.api.packaging.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;
import store.ckin.api.packaging.service.PackagingService;

/**
 * 포장지 정책 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/packaging")
public class PackagingController {

    private final PackagingService packagingService;

    /**
     * 포장지 정책을 생성하는 메서드입니다.
     *
     * @param requestDto
     * @return 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Void> createPackagingPolicy(@Valid @RequestBody PackagingCreateRequestDto requestDto) {

        log.info("requestDto = {}", requestDto);

        packagingService.createPackagingPolicy(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
