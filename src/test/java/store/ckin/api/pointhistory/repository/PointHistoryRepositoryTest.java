package store.ckin.api.pointhistory.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.pointhistory.dto.response.PointHistoryResponseDto;
import store.ckin.api.pointhistory.entity.PointHistory;

/**
 * 포인트 내역 레포지토리 테스트.
 *
 * @author 정승조
 * @version 2024. 03. 16.
 */

@DataJpaTest
class PointHistoryRepositoryTest {

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    TestEntityManager entityManager;

    Member member;

    Grade grade;


    @BeforeEach
    void setUp() {

        grade = Grade.builder()
                .id(1L)
                .name("Member")
                .pointRatio(10)
                .condition(0)
                .build();

        System.out.println("grade = " + grade);
        entityManager.persist(grade);

        member = Member.builder()
                .email("test@test.com")
                .contact("01012341234")
                .name("테스터")
                .point(0)
                .state(Member.State.ACTIVE)
                .birth(LocalDate.of(1999, 5, 13))
                .grade(grade)
                .build();

        entityManager.persist(member);

        entityManager.flush();
    }

    @Test
    @DisplayName("포인트 내역 저장 테스트")
    void testSave() {
        PointHistory pointHistory = PointHistory.builder()
                .member(member)
                .pointHistoryPoint(5000)
                .pointHistoryReason("회원가입")
                .pointHistoryTime(LocalDate.now())
                .build();


        PointHistory actual = pointHistoryRepository.save(pointHistory);

        assertAll(
                () -> assertNotNull(actual.getPointHistoryId()),
                () -> assertEquals(pointHistory.getMember(), actual.getMember()),
                () -> assertEquals(pointHistory.getPointHistoryPoint(), actual.getPointHistoryPoint()),
                () -> assertEquals(pointHistory.getPointHistoryReason(), actual.getPointHistoryReason()),
                () -> assertEquals(pointHistory.getPointHistoryTime(), actual.getPointHistoryTime())
        );
    }

    @Test
    @DisplayName("포인트 내역 리스트 조회 테스트")
    void testGetPointHistoryList() {
        PointHistory pointHistory = PointHistory.builder()
                .member(member)
                .pointHistoryPoint(5000)
                .pointHistoryReason("회원가입")
                .pointHistoryTime(LocalDate.now())
                .build();

        pointHistoryRepository.save(pointHistory);

        PointHistory pointHistory2 =
                PointHistory.builder().member(member).pointHistoryPoint(5000).pointHistoryReason("회원가입")
                        .pointHistoryTime(LocalDate.now()).build();

        pointHistoryRepository.save(pointHistory2);

        Pageable pageable = Pageable.ofSize(10);

        Page<PointHistoryResponseDto> acutal =
                pointHistoryRepository.getPointHistoryList(member.getId(), pageable);

        List<PointHistoryResponseDto> content = acutal.getContent();

        assertAll(
                () -> assertEquals(2, acutal.getTotalElements()),
                () -> assertEquals(0, acutal.getNumber()),
                () -> assertEquals(1, acutal.getTotalPages()),
                () -> assertEquals(10, acutal.getSize()),
                () -> assertEquals(2, content.size()),

                // order by desc
                () -> assertEquals(pointHistory.getPointHistoryId(), content.get(1).getId()),
                () -> assertEquals(pointHistory.getMember().getId(), content.get(1).getMemberId()),
                () -> assertEquals(pointHistory.getPointHistoryReason(), content.get(1).getPointHistoryReason()),
                () -> assertEquals(pointHistory.getPointHistoryPoint(), content.get(1).getPointHistoryPoint()),
                () -> assertEquals(pointHistory.getPointHistoryTime(), content.get(1).getPointHistoryTime()),
                () -> assertEquals(pointHistory2.getPointHistoryId(), content.get(0).getId()),
                () -> assertEquals(pointHistory2.getMember().getId(), content.get(0).getMemberId()),
                () -> assertEquals(pointHistory2.getPointHistoryReason(), content.get(0).getPointHistoryReason()),
                () -> assertEquals(pointHistory2.getPointHistoryPoint(), content.get(0).getPointHistoryPoint()),
                () -> assertEquals(pointHistory2.getPointHistoryTime(), content.get(0).getPointHistoryTime())
        );
    }
}