package store.ckin.api.review.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.member.entity.Member;
import store.ckin.api.member.exception.MemberNotFoundException;
import store.ckin.api.member.repository.MemberRepository;
import store.ckin.api.objectstorage.service.ObjectStorageService;
import store.ckin.api.review.dto.request.ReviewCreateRequestDto;
import store.ckin.api.review.entity.Review;
import store.ckin.api.review.repository.ReviewRepository;
import store.ckin.api.review.service.ReviewService;

import java.util.List;
import java.util.Optional;

/**
 * ReviewService
 *
 * @author 이가은
 * @version 2024. 03. 11.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ObjectStorageService objectStorageService;

    /**
     * 리뷰 업로드를 구현하는 메소드 입니다.
     *
     * @param createRequestDto 도서 아이디, 리뷰 점수, 리뷰 코멘트를 담고 있는 DTO 입니다.
     * @param imageList 리뷰의 이미지 리스트를 담고 있는 MultipartFile 리스트 입니다.
     */
    @Override
    public void postReview(ReviewCreateRequestDto createRequestDto, List<MultipartFile> imageList) {
        Optional<Member> member = memberRepository.findById(createRequestDto.getMemberId());

        if (member.isEmpty()) {
            throw new MemberNotFoundException(createRequestDto.getMemberId());
        }

        Optional<Book> book = bookRepository.findByBookId(createRequestDto.getBookId());

        if (book.isEmpty()) {
            throw new BookNotFoundException(createRequestDto.getBookId());
        }

        reviewRepository.save(Review.builder()
                .member(member.get())
                .book(book.get())
                .reviewRate(createRequestDto.getReviewRate())
                .reviewComment(createRequestDto.getReviewComment())
                .build());
        try {
            for (MultipartFile file : imageList) {
                objectStorageService.saveFile(file, "review");
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }
}
