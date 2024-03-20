package store.ckin.api.review.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.file.entity.File;
import store.ckin.api.file.repository.FileRepository;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.objectstorage.service.ObjectStorageService;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.dto.response.MyPageReviewResponseDto;
import store.ckin.api.review.dto.response.ReviewResponseDto;
import store.ckin.api.review.entity.Review;
import store.ckin.api.review.exception.SaveFileException;
import store.ckin.api.review.repository.ReviewRepository;
import store.ckin.api.review.service.ReviewService;

/**
 * ReviewService
 *
 * @author 이가은
 * @version 2024. 03. 11.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ObjectStorageService objectStorageService;
    private final FileRepository fileRepository;

    private static final String REVIEW_IMAGE_CATEGORY = "review";

    /**
     * 리뷰 업로드를 구현하는 메소드 입니다.
     *
     * @param createRequestDto 도서 아이디, 리뷰 점수, 리뷰 코멘트를 담고 있는 DTO 입니다.
     * @param imageList        리뷰의 이미지 리스트를 담고 있는 MultipartFile 리스트 입니다.
     */
    @Override
    @Transactional
    public void postReview(ReviewCreateRequestDto createRequestDto, List<MultipartFile> imageList) {
        Member member = memberRepository.findById(createRequestDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(createRequestDto.getMemberId()));

        Book book = bookRepository.findByBookId(createRequestDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(createRequestDto.getBookId()));

        book.updateBookReviewRate(createRequestDto.getReviewRate());

        Review review = reviewRepository.save(Review.builder()
                .member(member)
                .book(book)
                .reviewRate(createRequestDto.getReviewRate())
                .reviewComment(createRequestDto.getReviewComment())
                .build());

        if (Objects.nonNull(imageList)) {
            try {
                for (MultipartFile file : imageList) {
                    File reviewFile = objectStorageService.saveFile(file, REVIEW_IMAGE_CATEGORY);
                    fileRepository.save(reviewFile.toBuilder()
                            .review(review)
                            .build());
                }
            } catch (IOException e) {
                throw new SaveFileException();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param pageable 리뷰 페이지
     * @param bookId   도서 아이디
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReviewPageList(Pageable pageable, Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Page<ReviewResponseDto> reviewPage = reviewRepository.getReviewPageList(pageable, bookId);

        reviewPage.stream()
                .forEach(reviewResponseDto -> reviewResponseDto.setFilePath(
                        fileRepository.findFilePathByReviewId(reviewResponseDto.getReviewId())));
        return reviewPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MyPageReviewResponseDto> findReviewsByMemberWithPagination(Long memberId, Pageable pageable) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
        Page<MyPageReviewResponseDto> reviewPage =
                reviewRepository.findReviewsByMemberWithPagination(memberId, pageable);
        reviewPage.stream().forEach(reviewResponseDto -> reviewResponseDto.setFilePath(
                fileRepository.findFilePathByReviewId(reviewResponseDto.getReviewId())));
        return reviewPage;
    }

}
