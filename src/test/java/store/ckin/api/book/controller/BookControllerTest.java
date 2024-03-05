package store.ckin.api.book.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.service.BookService;
import store.ckin.api.objectstorage.service.ObjectStorageService;

/**
 * BookControllerTest.
 *
 * @author 나국로
 * @version 2024. 02. 29.
 */
@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private ObjectStorageService objectStorageService;


    @Test
    @DisplayName("ID로 책 조회")
    void givenBookId_whenGetBookById_thenReturnsBook() throws Exception {
        Long bookId = 1L;
        BookResponseDto bookResponseDto = BookResponseDto.builder()
                .bookId(1L)
                .bookIsbn("1234567890123")
                .bookTitle("테스트 책 제목")
                .bookDescription("이 책은 테스트를 위해 만들어진 책입니다.")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.of(2024, 1, 1))
                .bookIndex("목차1, 목차2, 목차3")
                .bookPackaging(true)
                .bookStock(50)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookState("판매중")
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .authorNames(List.of("저자1", "저자2"))
                .categoryNames(List.of("카테고리1", "카테고리2"))
                .tagNames(List.of("태그1", "태그2"))
                .build();


        when(bookService.findBookById(bookId)).thenReturn(bookResponseDto);

        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId));
    }

//    @Test
//    @DisplayName("책 생성 요청")
//    void givenBookInfo_whenCreateBook_thenCreatesBook() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        BookCreateRequestDto createRequestDto = new BookCreateRequestDto();
//        ReflectionTestUtils.setField(createRequestDto, "bookIsbn", "978-3-16-148410-0");
//        ReflectionTestUtils.setField(createRequestDto, "bookTitle", "테스트 책 제목");
//        ReflectionTestUtils.setField(createRequestDto, "bookDescription", "이 책은 테스트 목적으로 작성된 책입니다.");
//        ReflectionTestUtils.setField(createRequestDto, "bookPublisher", "테스트 출판사");
//        ReflectionTestUtils.setField(createRequestDto, "bookPublicationDate", LocalDate.of(2024, 1, 1));
//        ReflectionTestUtils.setField(createRequestDto, "bookIndex", "테스트 목차");
//        ReflectionTestUtils.setField(createRequestDto, "bookPackaging", true);
//        ReflectionTestUtils.setField(createRequestDto, "bookState", "판매중");
//        ReflectionTestUtils.setField(createRequestDto, "bookStock", 100);
//        ReflectionTestUtils.setField(createRequestDto, "bookRegularPrice", 20000);
//        ReflectionTestUtils.setField(createRequestDto, "bookDiscountRate", 10);
//        ReflectionTestUtils.setField(createRequestDto, "authorIds", new HashSet<>(Set.of(1L, 2L)));
//        ReflectionTestUtils.setField(createRequestDto, "categoryIds", new HashSet<>(Set.of(1L)));
//        ReflectionTestUtils.setField(createRequestDto, "tagIds", new HashSet<>(Set.of(1L, 2L)));
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createRequestDto)))
//                .andExpect(status().isCreated());
//    }


    @Test
    @DisplayName("책 정보 업데이트")
    void givenBookInfo_whenUpdateBook_thenUpdatesBook() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        long bookId = 1L;
        BookModifyRequestDto modifyRequestDto = new BookModifyRequestDto();
        ReflectionTestUtils.setField(modifyRequestDto, "bookIsbn", "978-3-16-148410-1");
        ReflectionTestUtils.setField(modifyRequestDto, "bookTitle", "수정된 책 제목");
        ReflectionTestUtils.setField(modifyRequestDto, "bookDescription", "이 책은 수정된 설명을 가지고 있습니다.");
        ReflectionTestUtils.setField(modifyRequestDto, "bookPublisher", "수정된 출판사");
        ReflectionTestUtils.setField(modifyRequestDto, "bookPublicationDate", LocalDate.of(2024, 2, 1));
        ReflectionTestUtils.setField(modifyRequestDto, "bookIndex", "수정된 목차");
        ReflectionTestUtils.setField(modifyRequestDto, "bookPackaging", false);
        ReflectionTestUtils.setField(modifyRequestDto, "bookState", "재고 있음");
        ReflectionTestUtils.setField(modifyRequestDto, "bookStock", 50);
        ReflectionTestUtils.setField(modifyRequestDto, "bookRegularPrice", 25000);
        ReflectionTestUtils.setField(modifyRequestDto, "bookDiscountRate", 15);
        ReflectionTestUtils.setField(modifyRequestDto, "authorIds", new HashSet<>(Set.of(3L)));
        ReflectionTestUtils.setField(modifyRequestDto, "categoryIds", new HashSet<>(Set.of(2L)));
        ReflectionTestUtils.setField(modifyRequestDto, "tagIds", new HashSet<>(Set.of(3L, 4L)));


        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("저자 이름으로 책 검색")
    void givenAuthorName_whenFindByAuthorName_thenReturnsBooks() throws Exception {
        String authorName = "저자1";
        List<BookListResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(BookListResponseDto.builder()
                .bookId(1L)
                .bookIsbn("1234567890123")
                .bookTitle("테스트 책 제목 1")
                .bookDescription("테스트 설명 1")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.of(2024, 1, 1))
                .bookIndex("목차 1")
                .bookPackaging(true)
                .bookStock(100)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookState("판매중")
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .authorNames(List.of("저자1", "저자2"))
                .build());

        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bookPublicationDate"));
        Page<BookListResponseDto> bookPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());

        when(bookService.findByAuthorName(authorName, pageable)).thenReturn(bookPage);

        mockMvc.perform(get("/api/books/search/by-author")
                        .param("authorName", authorName)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].bookId", is(1)))
                .andExpect(jsonPath("$.content[0].bookTitle", is("테스트 책 제목 1")))
                .andExpect(jsonPath("$.content[0].authorNames", containsInAnyOrder("저자1", "저자2")));
    }

    @Test
    @DisplayName("책 제목으로 책 검색")
    void givenBookTitle_whenFindByBookTitle_thenReturnsBooks() throws Exception {
        String title = "책 제목";
        List<BookListResponseDto> bookListResponseDtos = new ArrayList<>();
        bookListResponseDtos.add(BookListResponseDto.builder()
                .bookId(1L)
                .bookIsbn("1234567890123")
                .bookTitle("테스트 책 제목 1")
                .bookDescription("테스트 책 설명 1")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.of(2024, 1, 1))
                .bookIndex("목차 1")
                .bookPackaging(true)
                .bookStock(100)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookState("판매중")
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .authorNames(List.of("저자1", "저자2"))
                .build());

        PageRequest pageable = PageRequest.of(0, 10, Sort.by("bookPublicationDate").descending());
        Page<BookListResponseDto> bookPage =
                new PageImpl<>(bookListResponseDtos, pageable, bookListResponseDtos.size());


        when(bookService.findByBookTitle(title, pageable)).thenReturn(bookPage);

        mockMvc.perform(get("/api/books/search/by-title")
                        .param("title", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }


    @Test
    @DisplayName("카테고리 ID로 책 검색")
    void givenCategoryId_whenFindByCategoryId_thenReturnsBooks() throws Exception {
        Long categoryId = 1L;
        List<BookListResponseDto> bookListResponseDtos = new ArrayList<>();
        bookListResponseDtos.add(BookListResponseDto.builder()
                .bookId(1L)
                .bookIsbn("1234567890123")
                .bookTitle("테스트 책 제목 1")
                .bookDescription("테스트 책 설명 1")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.of(2024, 1, 1))
                .bookIndex("목차 1")
                .bookPackaging(true)
                .bookStock(100)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookState("판매중")
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .authorNames(List.of("저자1", "저자2"))
                .build());
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bookPublicationDate"));
        Page<BookListResponseDto> bookPage =
                new PageImpl<>(bookListResponseDtos, pageable, bookListResponseDtos.size());

        Mockito.when(bookService.findByCategoryId(categoryId, pageable)).thenReturn(bookPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search/by-category")
                        .param("categoryId", categoryId.toString())
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(bookListResponseDtos.size())));
    }

    @Test
    @DisplayName("모든 책 조회")
    void givenPageable_whenFindAllBooks_thenReturnsPagedBooks() throws Exception {
        List<BookListResponseDto> bookListResponseDtos = new ArrayList<>();
        bookListResponseDtos.add(BookListResponseDto.builder()
                .bookId(1L)
                .bookIsbn("1234567890123")
                .bookTitle("테스트 책 제목 1")
                .bookDescription("테스트 책 설명 1")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.of(2024, 1, 1))
                .bookIndex("목차 1")
                .bookPackaging(true)
                .bookStock(100)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookState("판매중")
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .authorNames(List.of("저자1", "저자2"))
                .build());
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bookPublicationDate"));
        Page<BookListResponseDto> bookPage =
                new PageImpl<>(bookListResponseDtos, pageable, bookListResponseDtos.size());

        Mockito.when(bookService.findAllBooks(pageable)).thenReturn(bookPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(bookListResponseDtos.size())));
    }


}