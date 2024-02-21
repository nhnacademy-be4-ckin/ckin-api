package store.ckin.api.grade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.grade.entity.Grade;

/**
 * Grade 에 관한 쿼리를 관리하는 인터페이스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 16.
 */
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
