package store.ckin.api.pointpolicy.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.pointpolicy.dto.request.CreatePointPolicyRequestDto;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;
import store.ckin.api.pointpolicy.service.PointPolicyService;

/**
 * 포인트 정책 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 02. 11.
 */

@RestController
@RequestMapping("/api/point-policies")
@RequiredArgsConstructor
public class PointPolicyController {

    private final PointPolicyService pointPolicyService;

    /**
     * 포인트 정책을 생성하는 메서드입니다.
     *
     * @param createPointPolicy 포인트 정책 생성 요청 DTO
     * @return 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Void> createPointPolicy(@Valid @RequestBody CreatePointPolicyRequestDto createPointPolicy) {

        pointPolicyService.createPointPolicy(createPointPolicy);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 포인트 정책 리스트를 조회하는 메서드입니다.
     *
     * @return 200 (Ok), 포인트 정책 리스트
     */
    @GetMapping
    public ResponseEntity<List<PointPolicyResponseDto>> getPointPolicies() {
        return ResponseEntity.ok(pointPolicyService.getPointPolicies());
    }

    /**
     * 포인트 정책을 삭제하는 메서드입니다.
     *
     * @param id 삭제할 포인트 정책 ID
     * @return 204 (No Content)
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePointPolicy(@PathVariable("id") Long id) {
        pointPolicyService.deletePointPolicy(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
