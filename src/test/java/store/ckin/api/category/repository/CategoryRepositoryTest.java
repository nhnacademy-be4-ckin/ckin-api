package store.ckin.api.category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collections;
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
import org.springframework.util.ReflectionUtils;
import store.ckin.api.author.entity.Author;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.book.relationship.booktag.entity.BookTag;
import store.ckin.api.category.entity.Category;
import store.ckin.api.tag.entity.Tag;


/**
 * CategoryRepositoryTest.
 *
 * @author 나국로
 * @version 2024. 02. 21.
 */
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

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
        BookCategory bookCategory =
                new BookCategory(new BookCategory.PK(testBook.getBookId(), category.getCategoryId()), testBook,
                        category);
        entityManager.persist(bookCategory);
        BookAuthor bookAuthor =
                new BookAuthor(new BookAuthor.PK(testBook.getBookId(), author.getAuthorId()), testBook, author);
        entityManager.persist(bookAuthor);

        BookTag bookTag = new BookTag(new BookTag.PK(testBook.getBookId(), tag.getTagId()), testBook, tag);
        entityManager.persist(bookTag);


        testBook = entityManager.persist(testBook);


        Field authorsField = ReflectionUtils.findField(Book.class, "authors");
        if (authorsField != null) {
            authorsField.setAccessible(true);
            Set<BookAuthor> authors = new HashSet<>();
            authors.add(bookAuthor);
            ReflectionUtils.setField(authorsField, testBook, authors);
        }
        Field categoriesField = ReflectionUtils.findField(Book.class, "categories");
        if (categoriesField != null) {
            categoriesField.setAccessible(true);
            Set<BookCategory> categories = new HashSet<>();
            categories.add(bookCategory);
            ReflectionUtils.setField(categoriesField, testBook, categories);
        }
        Field tagsField = ReflectionUtils.findField(Book.class, "tags");
        if (tagsField != null) {
            tagsField.setAccessible(true);
            Set<BookTag> tags = new HashSet<>();
            tags.add(bookTag);
            ReflectionUtils.setField(tagsField, testBook, tags);
        }

        entityManager.flush();
    }


    @Test
    @DisplayName("새 카테고리 생성 및 조회")
    void givenNewCategory_whenSave_thenFindById() {

        Category newCategory = Category.builder()
                .categoryName("국내도서")
                .categoryPriority(1)
                .build();

        categoryRepository.save(newCategory);

        Optional<Category> foundCategory = categoryRepository.findById(newCategory.getCategoryId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getCategoryName()).isEqualTo("국내도서");
        assertThat(foundCategory.get().getCategoryPriority()).isEqualTo(1);
    }

    @Test
    @DisplayName("카테고리 정보 업데이트 및 검증")
    void givenExistingCategory_whenUpdate_thenFindUpdatedCategory() {
        Category originalCategory = Category.builder()
                .categoryName("국내소설")
                .categoryPriority(2)
                .build();
        categoryRepository.save(originalCategory);

        Category updatedCategory = originalCategory.toBuilder()
                .categoryName("외국소설")
                .build();
        categoryRepository.save(updatedCategory);

        Optional<Category> foundCategory = categoryRepository.findById(updatedCategory.getCategoryId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getCategoryName()).isEqualTo("외국소설");
    }

    @Test
    @DisplayName("특정 ID로 카테고리 조회")
    void givenCategoryId_whenFindByCategoryId_thenCategoryIsReturned() {
        Category category = Category.builder()
                .categoryName("국내도서")
                .build();
        categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findByCategoryId(category.getCategoryId());

        assertThat(found).isPresent();
        assertThat(found.get().getCategoryName()).isEqualTo("국내도서");
    }

    @Test
    @DisplayName("최상위 카테고리 조회")
    void whenFindRootCategories_thenRootCategoriesAreReturned() {
        Category rootCategory = Category.builder()
                .categoryName("최상위 카테고리")
                .build();
        categoryRepository.save(rootCategory);

        List<Category> rootCategories = categoryRepository.findByParentCategoryIsNull();

        assertThat(rootCategories).isNotEmpty();
        assertThat(rootCategories.get(1).getCategoryName()).isEqualTo("최상위 카테고리");
    }

    @Test
    @DisplayName("부모 ID로 하위 카테고리 조회")
    void givenParentCategoryId_whenFindChildCategories_thenChildCategoriesAreReturned() {

        Category parentCategory = Category.builder()
                .categoryName("부모 카테고리")
                .build();
        categoryRepository.save(parentCategory);

        Category childCategory = Category.builder()
                .parentCategory(parentCategory)
                .categoryName("자식 카테고리")
                .build();
        categoryRepository.save(childCategory);

        List<Category> childCategories =
                categoryRepository.findByParentCategory_CategoryId(parentCategory.getCategoryId());

        assertThat(childCategories).isNotEmpty();
        assertThat(childCategories.get(0).getCategoryName()).isEqualTo("자식 카테고리");
    }


    @Test
    @DisplayName("존재하지 않는 ID로 카테고리 조회")
    void givenNonExistingCategoryId_whenFindByCategoryId_thenCategoryNotPresent() {
        Long nonExistingId = 999L;

        Optional<Category> result = categoryRepository.findById(nonExistingId);

        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("주어진 책 ID로 카테고리 ID 조회 시, 카테고리 ID 반환")
    void givenBookIds_whenGetParentIds_thenCategoryIdsReturned() {
        List<Long> testBookIds = Collections.singletonList(testBook.getBookId());

        List<Long> retrievedCategoryIds = categoryRepository.getParentIds(testBookIds);

        assertThat(retrievedCategoryIds).containsOnly(category.getCategoryId());
    }

}
