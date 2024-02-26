package store.ckin.api.book.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.entity.Book;

/**
 * BookRepository 인터페이스.
 * 
 * @author 나국로
 * @version 2024. 02. 26.
*/
public interface BookRepository extends JpaRepository<Book, Long> , BookRepositoryCustom {

}