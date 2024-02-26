package store.ckin.api.book.dto.request;

import java.util.Date;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * {class name}.
 * 
 * @author 나국로
 * @version 2024. 02. 26.
*/
@Getter
@NoArgsConstructor
public class BookCreateRequestDto {
    private String bookIsbn;
    private String bookTitle;
    private String bookDescription;
    private String bookPublisher;
    private Date bookPublicationDate;
    private String bookIndex;
    private Boolean bookPackaging;
    private String bookState;
    private Integer bookStock;
    private Integer bookRegularPrice;
    private Integer bookDiscountRate;
    private Set<Long> authorIds;
    private Set<Long> categoryIds;
    private Set<Long> tagIds;



}
