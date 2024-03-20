package store.ckin.api.book.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.ckin.api.author.entity.QAuthor;
import store.ckin.api.book.dto.response.BookExtractionResponseDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookMainPageResponseDto;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.entity.QBook;
import store.ckin.api.book.relationship.bookauthor.entity.QBookAuthor;
import store.ckin.api.book.relationship.bookcategory.dto.response.BookCategoryResponseDto;
import store.ckin.api.book.relationship.bookcategory.entity.QBookCategory;
import store.ckin.api.book.relationship.booktag.entity.QBookTag;
import store.ckin.api.book.repository.BookRepositoryCustom;
import store.ckin.api.category.entity.QCategory;
import store.ckin.api.file.entity.QFile;
import store.ckin.api.tag.entity.QTag;

/**
 * BookRepository의 구현클래스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
public class BookRepositoryImpl extends QuerydslRepositorySupport implements BookRepositoryCustom {
    private final EntityManager entityManager;

    public BookRepositoryImpl(EntityManager entityManager) {
        super(Book.class);
        this.entityManager = entityManager;
    }

    QBook book = QBook.book;
    QBookAuthor bookAuthor = QBookAuthor.bookAuthor;
    QAuthor author = QAuthor.author;
    QCategory category = QCategory.category;
    QTag tag = QTag.tag;
    QBookCategory bookCategory = QBookCategory.bookCategory;
    QBookTag bookTag = QBookTag.bookTag;
    QFile file = QFile.file;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BookListResponseDto> findByAuthorName(String authorName, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        List<Long> bookIds = queryFactory
                .select(book.bookId)
                .from(book)
                .join(book.authors, bookAuthor)
                .join(bookAuthor.author, author)
                .where(author.authorName.eq(authorName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.authors, bookAuthor).fetchJoin()
                .leftJoin(bookAuthor.author, author).fetchJoin()
                .leftJoin(book.thumbnail, file)
                .where(book.bookId.in(bookIds))
                .distinct()
                .fetch();


        Long total = Optional.ofNullable(queryFactory
                        .select(book.count())
                        .from(book)
                        .join(book.authors, bookAuthor)
                        .join(bookAuthor.author, author)
                        .where(author.authorName.eq(authorName))
                        .fetchOne())
                .orElse(0L);


        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BookListResponseDto> findByBookTitleContaining(String bookTitle, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Long> bookIds = queryFactory
                .select(book.bookId)
                .from(book)
                .where(book.bookTitle.containsIgnoreCase(bookTitle))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.authors, bookAuthor).fetchJoin()
                .leftJoin(bookAuthor.author, author).fetchJoin()
                .leftJoin(book.thumbnail, file)
                .where(book.bookId.in(bookIds))
                .distinct()
                .fetch();


        Long total = Optional.ofNullable(queryFactory
                        .select(book.count())
                        .from(book)
                        .where(book.bookTitle.containsIgnoreCase(bookTitle))
                        .fetchOne())
                .orElse(0L);

        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BookListResponseDto> findByCategoryId(Long categoryId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

// 먼저 페이징된 ID를 가져옵니다.
        List<Long> bookIds = queryFactory
                .select(book.bookId)
                .from(book)
                .join(book.categories, bookCategory)
                .where(bookCategory.category.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

// 이후에 해당 ID를 가진 책들을 조회합니다.
        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.authors, bookAuthor).fetchJoin()
                .leftJoin(bookAuthor.author, author).fetchJoin()
                .leftJoin(book.categories, bookCategory).fetchJoin()
                .leftJoin(bookCategory.category, category).fetchJoin()
                .leftJoin(book.thumbnail, file).fetchJoin()
                .where(book.bookId.in(bookIds))
                .distinct()
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(book.count())
                        .from(book)
                        .join(book.categories, bookCategory)
                        .where(bookCategory.category.categoryId.eq(categoryId))
                        .fetchOne())
                .orElse(0L);

        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BookListResponseDto> findAllBooks(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Long> bookIds = queryFactory
                .select(book.bookId)
                .from(book)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.authors, bookAuthor).fetchJoin()
                .leftJoin(bookAuthor.author, author).fetchJoin()
                .leftJoin(book.thumbnail, file).fetchJoin()
                .where(book.bookId.in(bookIds))
                .distinct()
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                        .select(book.count())
                        .from(book)
                        .fetchOne())
                .orElse(0L);

        List<BookListResponseDto> bookResponseDtos = books.stream()
                .map(this::convertToBookListResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, total);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Book> findByBookId(Long bookId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        Book resultBook = queryFactory
                .selectFrom(this.book)
                .leftJoin(this.book.authors, bookAuthor).fetchJoin()
                .leftJoin(bookAuthor.author, author).fetchJoin()
                .leftJoin(this.book.categories, bookCategory).fetchJoin()
                .leftJoin(bookCategory.category, category).fetchJoin()
                .leftJoin(this.book.tags, bookTag).fetchJoin()
                .leftJoin(bookTag.tag, tag).fetchJoin()
                .leftJoin(book.thumbnail, file)
                .where(this.book.bookId.eq(bookId))
                .fetchOne();

        return Optional.ofNullable(resultBook);
    }

    /**
     * {@inheritDoc}
     *
     * @param bookIds 도서 아이디 리스트
     * @return 도서 추출 정보 응답 DTO 리스트
     */
    @Override
    public List<BookExtractionResponseDto> getExtractBookListByBookIds(List<Long> bookIds) {


        List<BookExtractionResponseDto> bookInfoList
                = from(book)
                .join(book.categories, bookCategory)
                .join(book.thumbnail, file)
                .where(book.bookId.in(bookIds))
                .select(Projections.constructor(BookExtractionResponseDto.class,
                        book.bookId,
                        file.fileUrl,
                        book.bookTitle,
                        book.bookPackaging,
                        book.bookSalePrice,
                        book.bookStock))
                .distinct()
                .fetch();


        List<BookCategoryResponseDto> bookCategoryList = from(bookCategory)
                .select(Projections.constructor(BookCategoryResponseDto.class,
                        bookCategory.book.bookId,
                        bookCategory.category.categoryId))
                .where(bookCategory.book.bookId.in(bookIds))
                .fetch();


        bookInfoList.forEach(bookInfo -> bookCategoryList.forEach(bookCategoryDto -> {
            if (bookInfo.getBookId().equals(bookCategoryDto.getBookId())) {
                bookInfo.getCategoryIds().add(bookCategoryDto.getCategoryId());
            }
        }));

        return bookInfoList;
    }

    @Override
    public List<BookMainPageResponseDto> getMainPageResponseDtoByCategoryId(Long categoryId, Integer limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Long> bookIds = queryFactory
                .select(book.bookId)
                .from(book)
                .join(book.categories, bookCategory)
                .where(bookCategory.category.categoryId.eq(categoryId))
                .orderBy(book.bookPublicationDate.desc())
                .limit(limit)
                .fetch();

// 이후에 해당 ID를 가진 책들을 조회합니다.
        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.authors, bookAuthor).fetchJoin()
                .leftJoin(bookAuthor.author, author).fetchJoin()
                .leftJoin(book.categories, bookCategory).fetchJoin()
                .leftJoin(bookCategory.category, category).fetchJoin()
                .leftJoin(book.tags,bookTag).fetchJoin()
                .leftJoin(bookTag.tag, tag).fetchJoin()
                .leftJoin(book.thumbnail, file).fetchJoin()
                .where(book.bookId.in(bookIds))
                .distinct()
                .fetch();

        return books.stream()
                .map(this::convertToBookMainPageResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookMainPageResponseDto> getMainPageResponseDtoOrderByBookPublicationDate(Integer limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Long> bookIds = queryFactory
                .select(book.bookId)
                .from(book)
                .orderBy(book.bookPublicationDate.desc())
                .limit(limit)
                .fetch();

// 이후에 해당 ID를 가진 책들을 조회합니다.
        List<Book> books = queryFactory
                .selectFrom(book)
                .leftJoin(book.authors, bookAuthor).fetchJoin()
                .leftJoin(bookAuthor.author, author).fetchJoin()
                .leftJoin(book.categories, bookCategory).fetchJoin()
                .leftJoin(bookCategory.category, category).fetchJoin()
                .leftJoin(book.tags, bookTag).fetchJoin()
                .leftJoin(bookTag.tag, tag).fetchJoin()
                .leftJoin(book.thumbnail, file).fetchJoin()
                .where(book.bookId.in(bookIds))
                .distinct()
                .fetch();

        return books.stream()
                .map(this::convertToBookMainPageResponseDto)
                .collect(Collectors.toList());
    }


    private BookListResponseDto convertToBookListResponseDto(Book book) {
        List<String> authorNames = book.getAuthors().stream()
                .map(bookAuthorElement -> bookAuthorElement.getAuthor().getAuthorName())
                .collect(Collectors.toList());

        String thumbnailUrl = book.getThumbnail() != null ? book.getThumbnail().getFileUrl() : null;

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
                .bookSalePrice(book.getBookSalePrice())
                .bookReviewRate(book.getBookReviewRate())
                .authorNames(authorNames)
                .thumbnail(thumbnailUrl) // 썸네일 URL 추가
                .build();
    }

    private BookMainPageResponseDto convertToBookMainPageResponseDto(Book book) {
        List<String> authorNames = book.getAuthors().stream()
                .map(bookAuthorElement -> bookAuthorElement.getAuthor().getAuthorName())
                .collect(Collectors.toList());
        List<String> categoryNames = book.getCategories().stream()
                .map(bookAuthorElement -> bookAuthorElement.getCategory().getCategoryName())
                .collect(Collectors.toList());
        List<String> tagNames = book.getTags().stream()
                .map(bookAuthorElement -> bookAuthorElement.getTag().getTagName())
                .collect(Collectors.toList());

        String thumbnailUrl = book.getThumbnail() != null ? book.getThumbnail().getFileUrl() : null;

        return BookMainPageResponseDto.builder()
                .bookId(book.getBookId())
                .bookTitle(book.getBookTitle())
                .bookRegularPrice(book.getBookRegularPrice())
                .bookDiscountRate(book.getBookDiscountRate())
                .bookSalePrice(book.getBookSalePrice())
                .productCategories(categoryNames)
                .productAuthorNames(authorNames)
                .productTags(tagNames)
                .thumbnail(thumbnailUrl)
                .build();
    }
}
