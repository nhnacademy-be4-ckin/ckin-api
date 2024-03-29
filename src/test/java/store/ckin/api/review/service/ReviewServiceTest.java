package store.ckin.api.review.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.file.entity.File;
import store.ckin.api.file.repository.FileRepository;
import store.ckin.api.grade.entity.Grade;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.objectstorage.service.ObjectStorageService;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.request.ReviewUpdateRequestDto;
import store.ckin.api.review.dto.response.MyPageReviewResponseDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.entity.Review;
import store.ckin.api.review.exception.UnauthorizedReviewAccessException;
import store.ckin.api.review.repository.ReviewRepository;
import store.ckin.api.review.service.impl.ReviewServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * ReviewServiceTest
 *
 * @author : 이가은
 * @version : 2024. 03. 12
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ObjectStorageService objectStorageService;
    @Mock
    private FileRepository fileRepository;

    private Review review;
    private Member member;
    private Book book;
    private Grade grade;
    private File file;
    private MockMultipartFile multipartFile;
    private ReviewCreateRequestDto reviewCreateRequestDto;
    private ReviewResponseDto reviewResponseDto;
    private MyPageReviewResponseDto myPageReviewResponseDto;

    @BeforeEach
    void setUp() {
        reviewCreateRequestDto = new ReviewCreateRequestDto();
        ReflectionTestUtils.setField(reviewCreateRequestDto, "memberId", 1L);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "bookId", 1L);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "reviewRate", 5);
        ReflectionTestUtils.setField(reviewCreateRequestDto, "reviewComment", "인상 깊게 읽었습니다.");

        member = Member.builder()
                .id(1L)
                .grade(grade)
                .birth(LocalDate.of(2023, 9, 1))
                .email("ckin1234@naver.com")
                .password("ckin1234")
                .name("ckin")
                .contact("010-1234-5678")
                .point(500)
                .state(Member.State.ACTIVE)
                .accumulateAmount(30000)
                .role(Member.Role.MEMBER)
                .build();

        book = Book.builder()
                .bookTitle("사람은 무엇으로 사는가")
                .bookIsbn("1234567890123")
                .bookDescription("<p>이 책의 설명입니다.<p>")
                .bookPublisher("ckin출판사")
                .bookIndex("이 책의 목차입니다.")
                .bookPackaging(true)
                .bookState("ON_SALE")
                .bookStock(180)
                .bookRegularPrice(20000)
                .bookDiscountRate(10)
                .bookSalePrice(18000)
                .bookReviewRate("4")
                .build();

        review = new Review(1L, 5, "good", member, book);

        file = File.builder()
                .fileId("file1")
                .fileCategory("review")
                .fileExtension("png")
                .fileOriginName("ckinFile")
                .book(null)
                .review(review)
                .fileUrl("http://url/fileida")
                .build();

        myPageReviewResponseDto = MyPageReviewResponseDto.builder()
                .reviewId(1L)
                .author("Author")
                .message("Review Message")
                .reviewRate(5)
                .reviewDate("2024-03-14")
                .thumbnailPath("path/to/thumbnail")
                .bookId(1L)
                .bookTitle("Book Title")
                .build();
    }

    @Test
    @DisplayName("리뷰 업로드 테스트 : 성공")
    void testPostReview() throws IOException {
        String json = "{\"reviewId\":1,\"author\":\"***un0000@email.com\",\"message\":\"good\",\"reviewRate\":5,\"reviewDate\":\"2023-03-12\"}";
        multipartFile = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", json.getBytes(StandardCharsets.UTF_8));

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findByBookId(anyLong())).thenReturn(Optional.of(book));
        when(objectStorageService.saveFile(any(), anyString())).thenReturn(file);

        reviewService.postReview(reviewCreateRequestDto, List.of(multipartFile));

        verify(memberRepository, times(1))
                .findById(anyLong());
        verify(bookRepository, times(1))
                .findByBookId(anyLong());
        verify(objectStorageService, times(1))
                .saveFile(any(), anyString());
        verify(reviewRepository, times(1))
                .save(any());
        verify(fileRepository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("리뷰 업로드 테스트 : 실패")
    void testPostReview_memberX() {
        String json = "{\"reviewId\":1,\"author\":\"***un0000@email.com\",\"message\":\"good\",\"reviewRate\":5,\"reviewDate\":\"2023-03-12\"}";
        multipartFile = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", json.getBytes(StandardCharsets.UTF_8));
        List<MultipartFile> multipartFileList = List.of(multipartFile);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(MemberNotFoundException.class, () -> reviewService.postReview(reviewCreateRequestDto, multipartFileList));
    }

    @Test
    @DisplayName("리뷰 업로드 테스트 : 실패")
    void testPostReview_bookX() throws IOException {
        String json = "{\"reviewId\":1,\"author\":\"***un0000@email.com\",\"message\":\"good\",\"reviewRate\":5,\"reviewDate\":\"2023-03-12\"}";
        multipartFile = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", json.getBytes(StandardCharsets.UTF_8));
        List<MultipartFile> multipartFileList = List.of(multipartFile);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findByBookId(anyLong())).thenReturn(Optional.empty());


        Assertions.assertThrows(BookNotFoundException.class, () -> reviewService.postReview(reviewCreateRequestDto, multipartFileList));
    }

    @Test
    @DisplayName("리뷰 업로드 테스트 : 실패")
    void testPostReview_fileX() throws IOException {
        String json = "{\"reviewId\":1,\"author\":\"***un0000@email.com\",\"message\":\"good\",\"reviewRate\":5,\"reviewDate\":\"2023-03-12\"}";
        multipartFile = new MockMultipartFile("createRequestDto", "createRequestDto", "application/json", json.getBytes(StandardCharsets.UTF_8));
        List<MultipartFile> multipartFileList = List.of(multipartFile);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findByBookId(anyLong())).thenReturn(Optional.of(book));
        when(objectStorageService.saveFile(any(), anyString())).thenThrow(FileNotFoundException.class);

        Assertions.assertThrows(Exception.class, () -> reviewService.postReview(reviewCreateRequestDto, multipartFileList));
    }

    @Test
    @DisplayName("리뷰 목록 조회 테스트")
    void testGetReviewPageList() {
        reviewResponseDto = new ReviewResponseDto(1L, "***un0000@email.com", "good", 5, "2023-03-12");
        Page<ReviewResponseDto> page = new PageImpl<>(List.of(reviewResponseDto));

        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.getReviewPageList(any(), anyLong())).thenReturn(page);

        reviewService.getReviewPageList(Pageable.ofSize(5), 1L);

        verify(bookRepository, times(1))
                .existsById(anyLong());
        verify(reviewRepository, times(1))
                .getReviewPageList(any(), anyLong());
    }

    @Test
    @DisplayName("리뷰 목록 조회 테스트 : 실패")
    void testGetReviewPageList_X() {
        reviewResponseDto = new ReviewResponseDto(1L, "***un0000@email.com", "good", 5, "2023-03-12");
        Page<ReviewResponseDto> page = new PageImpl<>(List.of(reviewResponseDto));
        Pageable pageable = PageRequest.of(0, 5);

        when(bookRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(BookNotFoundException.class, () -> reviewService.getReviewPageList(pageable, 1L));

    }

    @Test
    @DisplayName("회원이 작성한 리뷰 목록 조회 테스트")
    void findReviewsByMemberWithPaginationTest() {
        // Given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<MyPageReviewResponseDto> content = Collections.singletonList(myPageReviewResponseDto);
        Page<MyPageReviewResponseDto> page = new PageImpl<>(content, pageable, content.size());
        List<String> filePaths = Arrays.asList("path/to/file1", "path/to/file2");

        when(memberRepository.existsById(memberId)).thenReturn(true);
        when(reviewRepository.findReviewsByMemberWithPagination(memberId, pageable)).thenReturn(page);
        when(fileRepository.findFilePathByReviewId(myPageReviewResponseDto.getReviewId())).thenReturn(filePaths);

        // When
        Page<MyPageReviewResponseDto> result = reviewService.findReviewsByMemberWithPagination(memberId, pageable);

        // Then
        verify(memberRepository).existsById(memberId);
        verify(reviewRepository).findReviewsByMemberWithPagination(memberId, pageable);
        verify(fileRepository, times(content.size())).findFilePathByReviewId(anyLong());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(filePaths, result.getContent().get(0).getFilePath());
    }

    @Test
    @DisplayName("회원이 작성한 리뷰 목록 조회 테스트: 실패")
    void findReviewsByMemberWithPaginationTest_X() {
        Pageable pageable = PageRequest.of(0, 10);

        when(memberRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(MemberNotFoundException.class,
                () -> reviewService.findReviewsByMemberWithPagination(1L, pageable));
    }


    @Test
    @DisplayName("리뷰 수정 테스트")
    void updateReviewTest() {
        // Given
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewUpdateRequestDto updateRequestDto = new ReviewUpdateRequestDto();
        ReflectionTestUtils.setField(updateRequestDto, "reviewId", reviewId);
        ReflectionTestUtils.setField(updateRequestDto, "reviewRate", 5);
        ReflectionTestUtils.setField(updateRequestDto, "reviewComment", "Updated comment");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When
        reviewService.updateReview(updateRequestDto, memberId);

        // Then
        verify(reviewRepository).findById(reviewId);
        verify(memberRepository).findById(memberId);
        assertEquals("Updated comment", review.getReviewComment());
        assertEquals(5, review.getReviewRate());
    }

    @Test
    @DisplayName("리뷰 수정 테스트: 실패")
    void updateReviewTest_X() {

        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewUpdateRequestDto updateRequestDto = new ReviewUpdateRequestDto();
        ReflectionTestUtils.setField(updateRequestDto, "reviewId", reviewId);
        ReflectionTestUtils.setField(updateRequestDto, "reviewRate", 5);
        ReflectionTestUtils.setField(updateRequestDto, "reviewComment", "Updated comment");

        Member anotherMember = Member.builder()
                .id(2L)
                .grade(null)
                .birth(LocalDate.of(2023, 9, 1))
                .email("ckin1234@naver.com")
                .password("ckin1234")
                .name("ckin")
                .contact("010-1234-5678")
                .point(500)
                .state(Member.State.ACTIVE)
                .accumulateAmount(30000)
                .role(Member.Role.MEMBER)
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(anotherMember));

        Assertions.assertThrows(UnauthorizedReviewAccessException.class,
                () -> reviewService.updateReview(updateRequestDto, memberId));
    }

}