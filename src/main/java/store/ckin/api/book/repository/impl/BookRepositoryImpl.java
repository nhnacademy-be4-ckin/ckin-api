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
import store.ckin.api.book.relationship.bookauthor.entity.QBookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.QBookCategory;
import store.ckin.api.book.relationship.booktag.entity.QBookTag;
import store.ckin.api.book.repository.BookRepositoryCustom;
import store.ckin.api.category.entity.QCategory;
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

        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin()
                .where(qAuthor.authorName.eq(authorName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(qBook.count())
                        .from(qBook)
                        .join(qBook.authors, qBookAuthor)
                        .join(qBookAuthor.author, qAuthor)
                        .where(qAuthor.authorName.eq(authorName))
                        .fetchOne())
                .orElse(0L);


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

        Long total = Optional.ofNullable(queryFactory
                        .select(qBook.count())
                        .from(qBook)
                        .where(qBook.bookTitle.containsIgnoreCase(bookTitle))
                        .fetchOne())
                .orElse(0L);

        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }


    @Override
    public Page<BookListResponseDto> findByCategoryId(Long categoryId, Pageable pageable) {
        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBook.categories, qBookCategory).fetchJoin()
                .where(qBookCategory.category.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(qBook.count())
                        .from(qBook)
                        .join(qBook.categories, qBookCategory)
                        .where(qBookCategory.category.categoryId.eq(categoryId))
                        .fetchOne())
                .orElse(0L);

        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }


    @Override
    public Page<BookListResponseDto> findAllBooks(Pageable pageable) {
        List<Book> books = queryFactory
                .selectFrom(qBook)
                .leftJoin(qBook.authors, qBookAuthor).fetchJoin()
                .leftJoin(qBookAuthor.author, qAuthor).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(qBook.count())
                        .from(qBook)
                        .fetchOne())
                .orElse(0L);

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
