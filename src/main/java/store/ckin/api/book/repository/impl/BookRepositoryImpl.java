package store.ckin.api.book.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import store.ckin.api.author.entity.QAuthor;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.entity.QBook;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.book.repository.BookRepositoryCustom;
import store.ckin.api.relationship.bookauthor.entity.QBookAuthor;
import store.ckin.api.relationship.bookcategory.entity.QBookCategory;
import store.ckin.api.relationship.booktag.entity.QBookTag;

/**
 * BookRepository의 구현체.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QBook qBook = QBook.book;
    QBookAuthor qBookAuthor = QBookAuthor.bookAuthor;
    QAuthor qAuthor = QAuthor.author;
    QBookCategory qBookCategory = QBookCategory.bookCategory;
    QBookTag qBookTag = QBookTag.bookTag;


    @Override
    public Page<BookResponseDto> findByAuthorName(String authorName, Pageable pageable) {

        // 책에 대한 쿼리를 구성
        List<Book> books = queryFactory.selectFrom(qBook)
                .join(qBook.authors, qBookAuthor)
                .join(qBookAuthor.author, qAuthor)
                .where(qAuthor.authorName.eq(authorName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 결과 수를 계산
        long total = queryFactory.selectFrom(qBook)
                .join(qBook.authors, qBookAuthor)
                .join(qBookAuthor.author, qAuthor)
                .where(qAuthor.authorName.eq(authorName))
                .fetchCount();

        // Book 엔티티를 BookResponseDto로 변환
        List<BookResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    @Override
    public Page<BookResponseDto> findByBookTitleContaining(String bookTitle, Pageable pageable) {
        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin()
                .leftJoin(qBook.categories, qBookCategory).fetchJoin()
                .leftJoin(qBook.tags, qBookTag).fetchJoin()
                .where(qBook.bookTitle.containsIgnoreCase(bookTitle))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qBook)
                .where(qBook.bookTitle.containsIgnoreCase(bookTitle))
                .fetchCount();

        List<BookResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    private BookResponseDto convertToBookResponseDto(Book book) {
        List<String> authorNames = book.getAuthors().stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .collect(Collectors.toList());
        List<String> categoryNames = book.getCategories().stream()
                .map(bookCategory -> bookCategory.getCategory().getCategoryName())
                .collect(Collectors.toList());
        List<String> tagNames = book.getTags().stream()
                .map(bookTag -> bookTag.getTag().getTagName())
                .collect(Collectors.toList());

        return BookResponseDto.builder()
                .bookId(book.getBookId())
                .bookIsbn(book.getBookIsbn())
                .bookTitle(book.getBookTitle())
                .bookDescription(book.getBookDescription())
                .bookPublisher(book.getBookPublisher())
                .bookPublicationDate(book.getBookPublicationDate())
                .bookIndex(book.getBookIndex())
                .bookPackaging(book.getBookPackaging())
                .bookStock(book.getBookStock())
                .bookRegularPrice(book.getBookRegularPrice())
                .bookDiscountRate(book.getBookDiscountRate())
                .bookState(book.getBookState())
                .authorNames(authorNames)
                .categoryNames(categoryNames)
                .tagNames(tagNames)
                .build();
    }
}
