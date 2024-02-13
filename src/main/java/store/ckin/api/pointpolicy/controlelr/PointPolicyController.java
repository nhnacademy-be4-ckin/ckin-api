package store.ckin.api.pointpolicy.controlelr;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.pointpolicy.dto.request.CreatePointPolicyRequestDto;
import store.ckin.api.pointpolicy.service.PointPolicyService;

/**
 * 포인트 정책 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 02. 11.
 */

@RestController
@RequestMapping("/api/point-policy")
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
}
