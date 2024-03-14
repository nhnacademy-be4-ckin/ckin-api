package store.ckin.api.pointpolicy.repository;

import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.pointpolicy.dto.response.PointPolicyResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * 포인트 정책 Repository Querydsl 사용할 메서드가 있는 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 02. 15.
 */

@NoRepositoryBean
public interface PointPolicyRepositoryCustom {

    /**
     * 포인트 정책의 ID와 이름을 통해 존재 여부를 파악하는 메서드입니다.
     *
     * @param id   포인트 정책 ID
     * @param name 포인트 정책 이릠
     * @return 포인트 정책 존재 여부
     */
    boolean existsPointPolicy(Long id, String name);

    /**
     * 포인트 정책 ID로 포인트 정책을 조회하는 메서드입니다.
     *
     * @param id 포인트 정책 ID
     * @return 조회된 포인트 정책 응답 DTO
     */
    Optional<PointPolicyResponseDto> getPointPolicyById(Long id);

    /**
     * 모든 포인트 정책을 조회하는 메서드입니다.
     *
     * @return 조회된 포인트 정책 응답 DTO 리스트
     */
    List<PointPolicyResponseDto> getPointPolicies();
}
