package store.ckin.api.member.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;

/**
 * MemberRepository 에 대한 Test 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 계정 존재 여부 테스트")
    void testExistsByEmail() {
        Grade grade = Grade.builder()
                .id(1L)
                .name("test")
                .pointRatio(20)
                .condition(0)
                .build();

        Grade mergedGrade = entityManager.merge(grade);

        Member member = Member.builder()
                .grade(mergedGrade)
                .email("test@test.com")
                .password("1234")
                .contact("12312312345")
                .birth(LocalDate.now())
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .build();

        entityManager.merge(member);

        assertTrue(memberRepository.existsByEmail("test@test.com"));
    }
}