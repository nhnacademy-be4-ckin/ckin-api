package store.ckin.api.deliverypolicy.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.deliverypolicy.dto.request.DeliveryPolicyCreateRequestDto;
import store.ckin.api.deliverypolicy.dto.response.DeliveryPolicyResponseDto;
import store.ckin.api.deliverypolicy.service.DeliveryPolicyService;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */
@RestController
@RequestMapping("/api/delivery-policies")
@RequiredArgsConstructor
public class DeliveryPolicyController {

    private final DeliveryPolicyService deliveryPolicyService;

    @GetMapping
    public ResponseEntity<List<DeliveryPolicyResponseDto>> getDeliveryPolicies() {
        return ResponseEntity.ok(deliveryPolicyService.getDeliveryPolicies());
    }

    @PostMapping
    public ResponseEntity<Void> createDeliveryPolicy(
            @Valid @RequestBody DeliveryPolicyCreateRequestDto createDeliveryPolicy) {
        deliveryPolicyService.createDeliveryPolicy(createDeliveryPolicy);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
