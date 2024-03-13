package store.ckin.api.deliverypolicy.controller;

import java.util.List;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyCreateRequestDto;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyUpdateRequestDto;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.service.DeliveryPolicyService;

/**
 * 배송비 정책 Controller.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@RestController
@RequestMapping("/api/delivery-policies")
@RequiredArgsConstructor
public class DeliveryPolicyController {

    private final DeliveryPolicyService deliveryPolicyService;

    /**
     * 배송비 정책 리스트를 조회하는 메서드입니다.
     *
     * @return 200(OK), 배송비 정책 응답 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<DeliveryPolicyResponseDto>> getDeliveryPolicies() {
        return ResponseEntity.ok(deliveryPolicyService.getDeliveryPolicies());
    }

    /**
     * 배송비 정책을 조회하는 메서드입니다.
     *
     * @param id 조회할 배송비 정책 ID
     * @return 200(OK), 배송비 정책 응답 DTO
     */
    @GetMapping("{id}")
    public ResponseEntity<DeliveryPolicyResponseDto> getDeliveryPolicy(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deliveryPolicyService.getDeliveryPolicy(id));
    }

    /**
     * 배송비 정책을 생성하는 메서드입니다.
     *
     * @param createDeliveryPolicy 생성할 배송비 정책 DTO
     * @return 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Void> createDeliveryPolicy(
            @Valid @RequestBody DeliveryPolicyCreateRequestDto createDeliveryPolicy) {
        deliveryPolicyService.createDeliveryPolicy(createDeliveryPolicy);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateDeliveryPolicy(@PathVariable("id") Long id,
                                                     @Valid @RequestBody
                                                     DeliveryPolicyUpdateRequestDto updateDeliveryPolicy) {

        deliveryPolicyService.updateDeliveryPolicy(id, updateDeliveryPolicy);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/activation")
    public ResponseEntity<DeliveryPolicyResponseDto> getActiveDeliveryPolicy() {
        return ResponseEntity.ok(deliveryPolicyService.getActiveDeliveryPolicy());
    }
}
