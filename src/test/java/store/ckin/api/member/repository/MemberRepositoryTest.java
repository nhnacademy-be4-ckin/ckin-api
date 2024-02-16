package store.ckin.api.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;



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
    void testExistsByEmail() {
        Grade grade = Grade.builder()
                .name("test")
                .pointRatio(20)
                .build();

        Grade mergedGrade = entityManager.merge(grade);

        Member member = Member.builder()
                .grade(mergedGrade)
                .email("test@test.com")
                .password("1234")
                .contact("12312312345")
                .birth(LocalDateTime.now())
                .latestLoginAt(LocalDateTime.now())
                .role(Member.Role.MEMBER)
                .point(5000)
                .build();

        entityManager.merge(member);

        assertTrue(memberRepository.existsByEmail("test@test.com"));
    }
}