package store.ckin.api.book.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ReflectionUtils;
import store.ckin.api.author.entity.Author;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.book.relationship.booktag.entity.BookTag;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.category.entity.Category;
import store.ckin.api.tag.entity.Tag;

/**
 * BookRepositoryTest.
 *
 * @author 나국로
 * @version 2024. 02. 29.
 */
@DataJpaTest
@Import(BookRepositoryImpl.class)
class BookRepositoryImplTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book testBook;
    private Author author;
    private Category category;
    private Tag tag;

    @BeforeEach
    void setup() {
        author = Author.builder()
                .authorName("테스트 작가")
                .build();
        entityManager.persist(author);

        tag = Tag.builder().tagName("테스트 태그").build();
        entityManager.persist(tag);

        category = Category.builder()
                .categoryName("테스트 카테고리")
                .categoryPriority(1)
                .build();
        entityManager.persist(category);


        testBook = Book.builder()
                .bookTitle("테스트 책 제목")
                .bookDescription("테스트 책 설명")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.now())
                .bookRegularPrice(10000)
                .build();
        testBook = entityManager.persist(testBook);

        BookAuthor bookAuthor =
                new BookAuthor(new BookAuthor.PK(testBook.getBookId(), author.getAuthorId()), testBook, author);
        entityManager.persist(bookAuthor);

        BookTag bookTag = new BookTag(new BookTag.PK(testBook.getBookId(), tag.getTagId()), testBook, tag);
        entityManager.persist(bookTag);

        BookCategory bookCategory =
                new BookCategory(new BookCategory.PK(testBook.getBookId(), category.getCategoryId()), testBook,
                        category);
        entityManager.persist(bookCategory);

        Field authorsField = ReflectionUtils.findField(Book.class, "authors");
        if (authorsField != null) {
            authorsField.setAccessible(true);
            Set<BookAuthor> authors = new HashSet<>();
            authors.add(bookAuthor);
            ReflectionUtils.setField(authorsField, testBook, authors);
        }
        entityManager.flush();
    }

    @Test
    @DisplayName("저자 이름으로 책 검색")
    void findByAuthorNameTest() {
        Page<BookListResponseDto> results = bookRepository.findByAuthorName("테스트 작가", PageRequest.of(0, 10));
        assertThat(results.getContent()).extracting("authorNames").contains(List.of("테스트 작가"));
    }

    @Test
    @DisplayName("책 제목으로 책 검색")
    void findByBookTitleContainingTest() {
        String title = "테스트";
        Page<BookListResponseDto> results = bookRepository.findByBookTitleContaining(title, PageRequest.of(0, 10));

        assertThat(results.getContent())
                .extracting("bookTitle")
                .contains(testBook.getBookTitle());
    }

    @Test
    @DisplayName("카테고리 ID로 책 검색")
    void findByCategoryIdTest() {
        // setup() 메서드에서 생성된 카테고리의 ID 가져오기
        Long categoryId = category.getCategoryId();
        Page<BookListResponseDto> results = bookRepository.findByCategoryId(categoryId, PageRequest.of(0, 10));

        assertThat(results.getContent())
                .isNotEmpty()
                .extracting("bookId")
                .contains(testBook.getBookId()); // 반환된 책들 중 testBook의 ID가 있는지 확인
    }

    @Test
    @DisplayName("모든 책 조회")
    void findAllBooksTest() {
        Page<BookListResponseDto> results = bookRepository.findAllBooks(PageRequest.of(0, 10));

        assertThat(results.getContent())
                .isNotEmpty()
                .extracting("bookId")
                .contains(testBook.getBookId());
    }

    @Test
    @DisplayName("특정 책 ID로 책 조회")
    void findByBookIdTest() {
        Optional<Book> result = bookRepository.findByBookId(testBook.getBookId());

        assertThat(result)
                .isPresent()
                .contains(testBook);
    }


}