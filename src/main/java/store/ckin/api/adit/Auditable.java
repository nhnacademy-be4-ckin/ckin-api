package store.ckin.api.adit;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 엔티티의 생성 시간을 추적하기 위한 공통 필드 'createdAt'을 포함한 Auditable 추상 클래스입니다.
 *
 * @author 나국로
 * @version 2024. 02. 28.
 */
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public abstract class Auditable {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}