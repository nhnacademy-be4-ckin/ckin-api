package store.ckin.api.book.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import store.ckin.api.author.entity.QAuthor;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.entity.QBook;
import store.ckin.api.book.repository.BookRepositoryCustom;
import store.ckin.api.category.entity.QCategory;
import store.ckin.api.relationship.bookauthor.entity.QBookAuthor;
import store.ckin.api.relationship.bookcategory.entity.QBookCategory;
import store.ckin.api.relationship.booktag.entity.QBookTag;
import store.ckin.api.tag.entity.QTag;

/**
 * BookRepository의 구현클래스.
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
    QCategory qCategory = QCategory.category;
    QTag qTag = QTag.tag;
    QBookCategory qBookCategory = QBookCategory.bookCategory;
    QBookTag qBookTag = QBookTag.bookTag;


    @Override
    public Page<BookListResponseDto> findByAuthorName(String authorName, Pageable pageable) {

        // 책에 대한 쿼리를 구성
        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin()
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
        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    @Override
    public Page<BookListResponseDto> findByBookTitleContaining(String bookTitle, Pageable pageable) {
        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin()
                .where(qBook.bookTitle.containsIgnoreCase(bookTitle))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(qBook)
                .where(qBook.bookTitle.containsIgnoreCase(bookTitle))
                .fetchCount();

        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    @Override
    public Page<BookListResponseDto> findByCategoryId(Long categoryId, Pageable pageable) {
        // 카테고리 ID에 따른 책 목록을 조회
        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
//                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin()
                .leftJoin(qBook.categories, qBookCategory).fetchJoin()
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 결과 수를 계산
        long total = queryFactory
                .selectFrom(qBook)
                .join(qBook.categories, qBookCategory)
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .fetchCount();

        // Book 엔티티를 BookResponseDto로 변환
        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }


    @Override
    public Page<BookListResponseDto> findAllBooks(Pageable pageable) {
        // 모든 책을 조회
        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin() // 저자 엔티티에 대한 fetchJoin 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 결과 수를 계산
        long total = queryFactory
                .selectFrom(qBook)
                .fetchCount();

        // Book 엔티티를 BookResponseDto로 변환
        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    @Override
    public Optional<Book> findByBookId(Long bookId) {
        Book book = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin()
                .leftJoin(qBook.categories, qBookCategory).fetchJoin()
                .leftJoin(qBookCategory.category, qCategory).fetchJoin()
                .leftJoin(qBook.tags, qBookTag).fetchJoin()
                .leftJoin(qBookTag.tag, qTag).fetchJoin()
                .where(qBook.bookId.eq(bookId))
                .fetchOne();

        return Optional.ofNullable(book);
    }


    private BookListResponseDto convertToBookListResponseDto(Book book) {
        List<String> authorNames = book.getAuthors().stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .collect(Collectors.toList());

        return BookListResponseDto.builder()
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
                .build();
    }
}
