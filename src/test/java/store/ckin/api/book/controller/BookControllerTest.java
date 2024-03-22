package store.ckin.api.book.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
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
@AutoConfigureRestDocs(uriHost = "133.186.247.149", uriPort = 7030)
@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private ObjectStorageService objectStorageService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("ID에 대한 책 정보 조회 성공")
    void givenBookId_whenGetBookById_thenReturnsBookInfo() throws Exception {
        Long bookId = 1L;
        BookResponseDto bookResponseDto = BookResponseDto.builder()
                .bookId(bookId)
                .bookIsbn("1234567890123")
                .bookTitle("테스트 책 제목")
                .bookDescription("테스트 책 설명")
                .bookPublisher("테스트 출판사")
                .bookPublicationDate(LocalDate.now())
                .bookIndex("테스트 목차")
                .bookPackaging(true)
                .bookStock(10)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookState("판매중")
                .bookSalePrice(18000)
                .bookReviewRate("4.5")
                .thumbnail("thumbnail/url.jpg")
                .authorNames(Arrays.asList("작가1", "작가2"))
                .categoryNames(Arrays.asList("카테고리1", "카테고리2"))
                .tagNames(Arrays.asList("태그1", "태그2"))
                .build();

        given(bookService.findBookById(bookId)).willReturn(bookResponseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/books/{bookId}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.bookIsbn").value(bookResponseDto.getBookIsbn()))
                .andExpect(jsonPath("$.bookTitle").value(bookResponseDto.getBookTitle()))
                .andExpect(jsonPath("$.bookDescription").value(bookResponseDto.getBookDescription()))
                .andExpect(jsonPath("$.bookPublisher").value(bookResponseDto.getBookPublisher()))
                .andExpect(jsonPath("$.bookPublicationDate").value(bookResponseDto.getBookPublicationDate().toString()))
                .andExpect(jsonPath("$.bookIndex").value(bookResponseDto.getBookIndex()))
                .andExpect(jsonPath("$.bookPackaging").value(bookResponseDto.getBookPackaging()))
                .andExpect(jsonPath("$.bookStock").value(bookResponseDto.getBookStock()))
                .andExpect(jsonPath("$.bookRegularPrice").value(bookResponseDto.getBookRegularPrice()))
                .andExpect(jsonPath("$.bookState").value(bookResponseDto.getBookState()))
                .andExpect(jsonPath("$.bookSalePrice").value(bookResponseDto.getBookSalePrice()))
                .andExpect(jsonPath("$.bookReviewRate").value(bookResponseDto.getBookReviewRate()))
                .andExpect(jsonPath("$.thumbnail").value(bookResponseDto.getThumbnail()))
                .andExpect(jsonPath("$.authorNames", hasSize(bookResponseDto.getAuthorNames().size())))
                .andExpect(jsonPath("$.categoryNames", hasSize(bookResponseDto.getCategoryNames().size())))
                .andExpect(jsonPath("$.tagNames", hasSize(bookResponseDto.getTagNames().size())))
                .andDo(document("book/getBookById/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("bookId").description("도서의 ID")
                        ),
                        responseFields(
                                fieldWithPath("bookId").description("도서 ID"),
                                fieldWithPath("bookIsbn").description("도서의 ISBN 번호"),
                                fieldWithPath("bookTitle").description("도서의 제목"),
                                fieldWithPath("bookDescription").description("도서의 설명"),
                                fieldWithPath("bookPublisher").description("도서의 출판사"),
                                fieldWithPath("bookPublicationDate").description("도서의 출판 날짜"),
                                fieldWithPath("bookIndex").description("도서의 목차"),
                                fieldWithPath("bookPackaging").description("도서의 포장 여부"),
                                fieldWithPath("bookStock").description("도서의 재고 수"),
                                fieldWithPath("bookRegularPrice").description("도서의 정가"),
                                fieldWithPath("bookDiscountRate").description("도서의 할인율"),
                                fieldWithPath("bookState").description("도서의 판매 상태"),
                                fieldWithPath("bookSalePrice").description("도서의 판매 가격"),
                                fieldWithPath("bookReviewRate").description("도서의 리뷰 평점"),
                                fieldWithPath("thumbnail").description("도서 썸네일 이미지 URL"),
                                fieldWithPath("authorNames").description("도서의 작가 이름 목록"),
                                fieldWithPath("categoryNames").description("도서의 카테고리 이름 목록"),
                                fieldWithPath("tagNames").description("도서에 연결된 태그 이름 목록")
                        )));
    }


    @Test
    @DisplayName("책 정보 업데이트")
    void givenBookInfo_whenUpdateBook_thenUpdatesBook() throws Exception {
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


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("book/updateBook/success",
                        pathParameters(
                                parameterWithName("bookId").description("업데이트할 책의 ID")
                        ),
                        requestFields(
                                fieldWithPath("bookIsbn").description("도서의 ISBN"),
                                fieldWithPath("bookTitle").description("도서 제목"),
                                fieldWithPath("bookDescription").description("도서 설명"),
                                fieldWithPath("bookPublisher").description("출판사 이름"),
                                fieldWithPath("bookPublicationDate").description("도서의 출판일"),
                                fieldWithPath("bookIndex").description("도서의 목차"),
                                fieldWithPath("bookPackaging").description("도서의 포장 여부"),
                                fieldWithPath("bookState").description("도서의 현재 상태"),
                                fieldWithPath("bookStock").description("도서의 재고 수"),
                                fieldWithPath("bookRegularPrice").description("도서의 정가"),
                                fieldWithPath("bookDiscountRate").description("도서의 할인율"),
                                fieldWithPath("authorIds").description("도서 저자의 ID 목록"),
                                fieldWithPath("categoryIds").description("도서 카테고리의 ID 목록"),
                                fieldWithPath("tagIds").description("도서와 관련된 태그 ID 목록").optional()
                        )
                ));

    }


    @Test
    @DisplayName("도서 생성")
    void whenCreateBook_thenReturnStatusCreated() throws Exception {
        // DTO 객체 생성
        BookCreateRequestDto requestDto = new BookCreateRequestDto();

        // ReflectionTestUtils를 사용하여 필드에 데이터 설정
        ReflectionTestUtils.setField(requestDto, "bookIsbn", "123456789");
        ReflectionTestUtils.setField(requestDto, "bookTitle", "책 제목");
        ReflectionTestUtils.setField(requestDto, "bookDescription", "책 설명");
        ReflectionTestUtils.setField(requestDto, "bookPublisher", "출판사");
        ReflectionTestUtils.setField(requestDto, "bookPublicationDate", LocalDate.now());
        ReflectionTestUtils.setField(requestDto, "bookIndex", "책 목차");
        ReflectionTestUtils.setField(requestDto, "bookPackaging", true);
        ReflectionTestUtils.setField(requestDto, "bookState", "판매중");
        ReflectionTestUtils.setField(requestDto, "bookStock", 10);
        ReflectionTestUtils.setField(requestDto, "bookRegularPrice", 20000);
        ReflectionTestUtils.setField(requestDto, "bookDiscountRate", 10);
        ReflectionTestUtils.setField(requestDto, "authorIds", Set.of(1L, 2L));
        ReflectionTestUtils.setField(requestDto, "categoryIds", Set.of(1L, 2L));
        ReflectionTestUtils.setField(requestDto, "tagIds", Set.of(1L, 2L));

        // DTO를 JSON 문자열로 변환
        String bookCreateRequestDtoJson = objectMapper.writeValueAsString(requestDto);

        // MockMultipartFile 형태로 JSON 데이터를 담은 파트 생성
        MockMultipartFile requestDtoPart = new MockMultipartFile(
                "requestDto", "", "application/json", bookCreateRequestDtoJson.getBytes());

        // 파일 모의 객체 생성
        MockMultipartFile filePart = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "<<jpeg data>>".getBytes());

        mockMvc.perform(multipart("/api/books")
                        .file(filePart)
                        .file(requestDtoPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(document("book/createBook/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("requestDto").description("도서 생성에 필요한 상세 정보"),
                                partWithName("file").description("도서의 썸네일 이미지 파일")
                        ),
                        requestPartFields("requestDto",
                                fieldWithPath("bookIsbn").description("도서의 ISBN"),
                                fieldWithPath("bookTitle").description("도서 제목"),
                                fieldWithPath("bookDescription").description("도서 설명"),
                                fieldWithPath("bookPublisher").description("출판사 이름"),
                                fieldWithPath("bookPublicationDate").description("도서의 출판일"),
                                fieldWithPath("bookIndex").description("도서의 목차"),
                                fieldWithPath("bookPackaging").description("도서의 포장 여부"),
                                fieldWithPath("bookState").description("도서의 현재 상태"),
                                fieldWithPath("bookStock").description("도서의 재고 수"),
                                fieldWithPath("bookRegularPrice").description("도서의 정가"),
                                fieldWithPath("bookDiscountRate").description("도서의 할인율"),
                                fieldWithPath("authorIds").description("도서 저자의 ID 목록"),
                                fieldWithPath("categoryIds").description("도서 카테고리의 ID 목록"),
                                fieldWithPath("tagIds").description("도서와 관련된 태그 ID 목록").optional()
                        )
                ));

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

        mockMvc.perform(get("/api/books/search/by-category")
                        .param("categoryId", categoryId.toString())
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(bookListResponseDtos.size())))
                .andDo(document("book/findByCategoryId",
                        requestParameters(
                                parameterWithName("categoryId").description("검색할 카테고리의 ID"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("content[].bookIsbn").description("도서의 ISBN"),
                                fieldWithPath("content[].bookId").description("도서 ID"),
                                fieldWithPath("content[].bookTitle").description("도서 제목"),
                                fieldWithPath("content[].bookDescription").description("도서 설명"),
                                fieldWithPath("content[].bookPublisher").description("출판사"),
                                fieldWithPath("content[].bookPublicationDate").description("도서 출판 날짜"),
                                fieldWithPath("content[].bookIndex").description("도서 목차"),
                                fieldWithPath("content[].bookPackaging").description("도서 포장 여부"),
                                fieldWithPath("content[].bookStock").description("도서 재고 수"),
                                fieldWithPath("content[].bookRegularPrice").description("도서 정가"),
                                fieldWithPath("content[].bookDiscountRate").description("도서 할인율"),
                                fieldWithPath("content[].bookState").description("도서 상태"),
                                fieldWithPath("content[].bookSalePrice").description("도서 판매 가격"),
                                fieldWithPath("content[].bookReviewRate").description("도서 리뷰 평점"),
                                fieldWithPath("content[].authorNames").description("도서의 저자 목록"),
                                fieldWithPath("content[].thumbnail").description("도서의 썸네일 이미지 URL").optional(),
                                subsectionWithPath("pageable").ignored(),
                                fieldWithPath("totalElements").description("전체 도서 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("last").description("현재 페이지가 마지막 페이지인지 여부"),
                                fieldWithPath("first").description("현재 페이지가 첫 번째 페이지인지 여부"),
                                fieldWithPath("sort.empty").description("정렬된 데이터가 비어 있는지 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않은 데이터인지 여부"),
                                fieldWithPath("sort.sorted").description("정렬된 데이터인지 여부"),
                                fieldWithPath("size").description("페이지당 요소의 수"),
                                fieldWithPath("numberOfElements").description("현재 페이지의 요소 수"),
                                fieldWithPath("empty").description("현재 페이지가 비어 있는지 여부")
                        )
                ));
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

        mockMvc.perform(get("/api/books")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(bookListResponseDtos.size())));
    }


}