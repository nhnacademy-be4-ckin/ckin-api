package store.ckin.api.grade.domain.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 등급 관련 요청 시 필요한 DTO 클래스 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 20.
 */
@Getter
@NoArgsConstructor
public class GradeCreateRequestDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @Min(0)
    @Max(100)
    private Integer pointRatio;

    @NotNull
    private Integer condition;
}
