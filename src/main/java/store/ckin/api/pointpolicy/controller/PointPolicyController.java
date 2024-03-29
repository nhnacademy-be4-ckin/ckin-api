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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.pointpolicy.dto.request.PointPolicyCreateRequestDto;
import store.ckin.api.pointpolicy.dto.request.PointPolicyUpdateRequestDto;
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
     * 포인트 정책 개별 조회를 하는 메서드입니다.
     *
     * @param id 조회할 포인트 정책 ID
     * @return 조회된 포인트 정책 응답 DTO
     */
    @GetMapping("{id}")
    public ResponseEntity<PointPolicyResponseDto> getPointPolicy(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pointPolicyService.getPointPolicy(id));

    }

    /**
     * 포인트 정책 리스트를 조회하는 메서드입니다.
     *
     * @return 200 (OK), 포인트 정책 응답 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<PointPolicyResponseDto>> getPointPolicies() {
        return ResponseEntity.ok(pointPolicyService.getPointPolicies());
    }

    /**
     * 포인트 정책을 생성하는 메서드입니다.
     *
     * @param createPointPolicy 포인트 정책 생성 요청 DTO
     * @return 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Void> createPointPolicy(@Valid @RequestBody PointPolicyCreateRequestDto createPointPolicy) {

        pointPolicyService.createPointPolicy(createPointPolicy);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 포인트 정책을 수정하는 메서드입니다.
     *
     * @param id                수정할 포인트 정책 ID
     * @param updatePointPolicy 수정할 포인트 정책 요청 DTO
     * @return 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePointPolicy(@PathVariable("id") Long id,
                                                  @Valid @RequestBody PointPolicyUpdateRequestDto updatePointPolicy) {
        pointPolicyService.updatePointPolicy(id, updatePointPolicy);
        return ResponseEntity.ok().build();
    }

    /**
     * 포인트 정책을 삭제하는 메서드입니다.
     *
     * @param id 삭제할 포인트 정책 ID
     * @return 200 (OK)
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePointPolicy(@PathVariable("id") Long id) {
        pointPolicyService.deletePointPolicy(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
