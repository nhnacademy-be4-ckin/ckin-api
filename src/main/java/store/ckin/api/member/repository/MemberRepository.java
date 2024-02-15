package store.ckin.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.member.entity.Member;

/**
 * Member 에 관한 쿼리를 관리하는 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
