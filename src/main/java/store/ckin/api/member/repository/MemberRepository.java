package store.ckin.api.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.member.entity.Member;

/**
 * Member 에 관한 쿼리를 관리하는 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByEmail(String email);

    boolean existsByOauthId(String oauthId);

    /**
     * 이메일로 Member 를 조회하는 메서드 입니다.
     *
     * @param email 이메일
     * @return Member
     */
    Optional<Member> findByEmail(String email);
}
