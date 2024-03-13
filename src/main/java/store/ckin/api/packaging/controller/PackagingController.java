package store.ckin.api.packaging.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.ckin.api.packaging.dto.request.PackagingCreateRequestDto;
import store.ckin.api.packaging.dto.request.PackagingUpdateRequestDto;
import store.ckin.api.packaging.dto.response.PackagingResponseDto;
import store.ckin.api.packaging.service.PackagingService;

import javax.validation.Valid;
import java.util.List;

/**
 * 포장 정책 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 02. 20.
 */


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/packaging")
public class PackagingController {

    private final PackagingService packagingService;

    /**
     * 포장 정책을 생성하는 메서드입니다.
     *
     * @param requestDto 포장 정책 생성 요청 DTO
     * @return 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Void> createPackagingPolicy(@Valid @RequestBody PackagingCreateRequestDto requestDto) {
        packagingService.createPackagingPolicy(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 포장 정책 리스트를 조회하는 메서드입니다.
     *
     * @return 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<PackagingResponseDto>> getPackagingPolicies() {
        return ResponseEntity.ok(packagingService.getPackagingPolicies());
    }

    /**
     * 포장 정책을 조회하는 메서드입니다.
     *
     * @param id 조회할 포장 정책 ID
     * @return 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PackagingResponseDto> getPackagingPolicy(@PathVariable("id") Long id) {
        return ResponseEntity.ok(packagingService.getPackagingPolicy(id));
    }


    /**
     * 포장 정책을 수정하는 메서드입니다.
     *
     * @param requestDto 포장 정책 수정 요청 DTO
     * @return 200 (OK)
     */
    @PutMapping
    public ResponseEntity<Void> updatePackagingPolicy(@Valid @RequestBody PackagingUpdateRequestDto requestDto) {
        packagingService.updatePackagingPolicy(requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 포장 정책을 삭제하는 메서드입니다.
     *
     * @param id 삭제할 포장 정책 ID
     * @return 200 (OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackagingPolicy(@PathVariable("id") Long id) {
        packagingService.deletePackagingPolicy(id);
        return ResponseEntity.ok().build();
    }


}
