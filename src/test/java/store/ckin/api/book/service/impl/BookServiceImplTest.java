package store.ckin.api.book.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.repository.AuthorRepository;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.book.relationship.booktag.entity.BookTag;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.category.entity.Category;
import store.ckin.api.category.repository.CategoryRepository;
import store.ckin.api.tag.entity.Tag;
import store.ckin.api.tag.repository.TagRepository;

/**
 * BookServiceTest.
 *
 * @author 나국로
 * @version 2024. 02. 29.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TagRepository tagRepository;
    private Pageable pageable;
    BookListResponseDto book1;
    BookListResponseDto book2;
    private Page<BookListResponseDto> bookPage;


    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        book1 = BookListResponseDto.builder()
                .bookId(1L)
                .bookIsbn("1234567890123")
                .bookTitle("책 제목1")
                .bookDescription("책 설명1")
                .bookPublisher("출판사1")
                .bookPublicationDate(LocalDate.now())
                .bookIndex("목차1")
                .bookPackaging(true)
                .bookStock(10)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookState("판매중")
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .authorNames(List.of("작가1"))
                .build();

        book2 = BookListResponseDto.builder()
                .bookId(2L)
                .bookIsbn("9876543210987")
                .bookTitle("책 제목2")
                .bookDescription("책 설명2")
                .bookPublisher("출판사2")
                .bookPublicationDate(LocalDate.now())
                .bookIndex("목차2")
                .bookPackaging(false)
                .bookStock(5)
                .bookRegularPrice(15000)
                .bookDiscountRate(5)
                .bookState("재고 있음")
                .bookSalePrice(14250)
                .bookReviewRate("4.0")
                .authorNames(List.of("작가2"))
                .build();
        bookPage = new PageImpl<>(List.of(book1, book2), pageable, 2);


    }

//    @Test
//    @DisplayName("책 생성 성공")
//    void givenNewBookInfo_whenCreateBook_thenBookIsSuccessfullyCreated() {
//
//        Author author = Author.builder().authorId(1L).authorName("테스트 작가").build();
//        Category category = Category.builder().categoryId(1L).categoryName("테스트 카테고리").build();
//        Tag tag = Tag.builder().tagId(1L).tagName("테스트 태그").build();
//
//        BookCreateRequestDto requestDto = new BookCreateRequestDto();
//
//        ReflectionTestUtils.setField(requestDto, "bookIsbn", "1234567890123");
//        ReflectionTestUtils.setField(requestDto, "bookTitle", "테스트 책 제목");
//        ReflectionTestUtils.setField(requestDto, "bookDescription", "테스트 책 설명");
//        ReflectionTestUtils.setField(requestDto, "bookPublisher", "테스트 출판사");
//        ReflectionTestUtils.setField(requestDto, "bookPublicationDate", LocalDate.now());
//        ReflectionTestUtils.setField(requestDto, "bookRegularPrice", 10000);
//        ReflectionTestUtils.setField(requestDto, "bookDiscountRate", 10);
//        ReflectionTestUtils.setField(requestDto, "authorIds", new HashSet<>(Set.of(1L)));
//        ReflectionTestUtils.setField(requestDto, "categoryIds", new HashSet<>(Set.of(1L)));
//        ReflectionTestUtils.setField(requestDto, "tagIds", new HashSet<>(Set.of(1L)));
//
//        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
//        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
//        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));
//        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//
////        bookService.createBook(requestDto,);
//
//        verify(bookRepository).save(any(Book.class));
//        verify(authorRepository).findById(anyLong());
//        verify(categoryRepository).findById(anyLong());
//        verify(tagRepository).findById(anyLong());
//    }

    @Test
    @DisplayName("책 정보 수정 성공")
    void givenExistingBook_whenUpdateBook_thenBookIsSuccessfullyUpdated() {
        BookModifyRequestDto modifyRequestDto = new BookModifyRequestDto();

        ReflectionTestUtils.setField(modifyRequestDto, "bookIsbn", "9876543210987");
        ReflectionTestUtils.setField(modifyRequestDto, "bookTitle", "수정된 책 제목");
        ReflectionTestUtils.setField(modifyRequestDto, "bookDescription", "수정된 책 설명");
        ReflectionTestUtils.setField(modifyRequestDto, "bookPublisher", "수정된 출판사");
        ReflectionTestUtils.setField(modifyRequestDto, "bookPublicationDate", LocalDate.of(2024, 2, 15));
        ReflectionTestUtils.setField(modifyRequestDto, "bookIndex", "수정된 목차");
        ReflectionTestUtils.setField(modifyRequestDto, "bookPackaging", true);
        ReflectionTestUtils.setField(modifyRequestDto, "bookState", "IN_STOCK");
        ReflectionTestUtils.setField(modifyRequestDto, "bookStock", 50);
        ReflectionTestUtils.setField(modifyRequestDto, "bookRegularPrice", 12000);
        ReflectionTestUtils.setField(modifyRequestDto, "bookDiscountRate", 15);
        ReflectionTestUtils.setField(modifyRequestDto, "authorIds", new HashSet<>(Set.of(1L, 2L)));
        ReflectionTestUtils.setField(modifyRequestDto, "categoryIds", new HashSet<>(Set.of(3L, 4L)));
        ReflectionTestUtils.setField(modifyRequestDto, "tagIds", new HashSet<>(Set.of(5L, 6L)));

        Book existingBook = Book.builder()
                .bookId(1L)
                .bookIsbn("1234567890123")
                .bookTitle("기존 책 제목")
                .bookDescription("기존 책 설명")
                .bookPublisher("기존 출판사")
                .bookPublicationDate(LocalDate.now())
                .bookRegularPrice(10000)
                .bookDiscountRate(10)
                .authors(new HashSet<>())
                .categories(new HashSet<>())
                .tags(new HashSet<>())
                .build();

        Author author1 = Author.builder().authorId(1L).authorName("테스트 작가1").build();
        Author author2 = Author.builder().authorId(2L).authorName("테스트 작가2").build();
        Category category1 = Category.builder().categoryId(3L).categoryName("테스트 카테고리1").build();
        Category category2 = Category.builder().categoryId(4L).categoryName("테스트 카테고리2").build();
        Tag tag1 = Tag.builder().tagId(5L).tagName("테스트 태그1").build();
        Tag tag2 = Tag.builder().tagId(6L).tagName("테스트 태그2").build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(author2));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(4L)).thenReturn(Optional.of(category2));
        when(tagRepository.findById(5L)).thenReturn(Optional.of(tag1));
        when(tagRepository.findById(6L)).thenReturn(Optional.of(tag2));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        bookService.updateBook(1L, modifyRequestDto);

        verify(bookRepository).save(any(Book.class));
        verify(authorRepository, times(2)).findById(anyLong());
        verify(categoryRepository, times(2)).findById(anyLong());
        verify(tagRepository, times(2)).findById(anyLong());

    }


    @Test
    @DisplayName("저자 이름으로 책 검색 성공")
    void givenAuthorName_whenFindByAuthorName_thenReturnsBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<BookListResponseDto> books = List.of(book1);
        Page<BookListResponseDto> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findByAuthorName("작가", pageable)).thenReturn(bookPage);

        Page<BookListResponseDto> result = bookService.findByAuthorName("작가", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByAuthorName("작가", pageable);
    }

    @Test
    @DisplayName("책 제목으로 책 검색 성공")
    void givenBookTitle_whenFindByBookTitle_thenReturnsBooks() {
        when(bookRepository.findByBookTitleContaining("제목", pageable)).thenReturn(bookPage);

        Page<BookListResponseDto> result = bookService.findByBookTitle("제목", pageable);

        assertThat(result.getContent()).hasSize(2);
        verify(bookRepository).findByBookTitleContaining("제목", pageable);
    }

    @Test
    @DisplayName("카테고리 ID로 책 검색 성공")
    void givenCategoryId_whenFindByCategoryId_thenReturnsBooks() {
        when(bookRepository.findByCategoryId(1L, pageable)).thenReturn(bookPage);

        Page<BookListResponseDto> result = bookService.findByCategoryId(1L, pageable);

        assertThat(result.getContent()).hasSize(2);
        verify(bookRepository).findByCategoryId(1L, pageable);
    }

    @Test
    @DisplayName("모든 책 검색 성공")
    void whenFindAllBooks_thenReturnsAllBooks() {
        when(bookRepository.findAllBooks(pageable)).thenReturn(bookPage);

        Page<BookListResponseDto> result = bookService.findAllBooks(pageable);

        assertThat(result.getContent()).hasSize(2);
        verify(bookRepository).findAllBooks(pageable);
    }

    @Test
    @DisplayName("책 ID로 조회 성공")
    void givenBookId_whenFindBookById_thenBookFound() {
        Long bookId = 1L;

        Set<BookAuthor> authors = new HashSet<>();
        Set<BookCategory> categories = new HashSet<>();
        Set<BookTag> tags = new HashSet<>();

        Book book = Book.builder()
                .bookId(bookId)
                .bookIsbn("1234567890")
                .bookTitle("테스트 책")
                .bookDescription("테스트 설명")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.now())
                .bookIndex("목차")
                .bookPackaging(true)
                .bookStock(100)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .authors(authors)
                .categories(categories)
                .tags(tags)
                .build();

        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(book));

        BookResponseDto responseDto = bookService.findBookById(bookId);

        assertNotNull(responseDto);
        assertEquals(book.getBookId(), responseDto.getBookId());
        verify(bookRepository).findByBookId(bookId);
    }


    @Test
    @DisplayName("책 ID로 조회 실패 -> BookNotFoundException 예외 처리")
    void givenInvalidBookId_whenFindBookById_thenThrowsException() {
        Long bookID = 99L;
        when(bookRepository.findByBookId(bookID)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBookById(bookID));

        verify(bookRepository).findByBookId(bookID);
    }


}