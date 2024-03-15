package store.ckin.api.book.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.author.entity.Author;
import store.ckin.api.author.exception.AuthorNotFoundException;
import store.ckin.api.author.repository.AuthorRepository;
import store.ckin.api.book.dto.request.BookCreateRequestDto;
import store.ckin.api.book.dto.request.BookModifyRequestDto;
import store.ckin.api.book.dto.response.BookExtractionResponseDto;
import store.ckin.api.book.dto.response.BookListResponseDto;
import store.ckin.api.book.dto.response.BookMainPageResponseDto;
import store.ckin.api.book.dto.response.BookResponseDto;
import store.ckin.api.book.entity.Book;
import store.ckin.api.book.exception.BookNotFoundException;
import store.ckin.api.book.relationship.bookauthor.entity.BookAuthor;
import store.ckin.api.book.relationship.bookcategory.entity.BookCategory;
import store.ckin.api.book.relationship.booktag.entity.BookTag;
import store.ckin.api.book.repository.BookRepository;
import store.ckin.api.book.service.BookService;
import store.ckin.api.category.entity.Category;
import store.ckin.api.category.exception.CategoryNotFoundException;
import store.ckin.api.category.repository.CategoryRepository;
import store.ckin.api.file.entity.File;
import store.ckin.api.file.repository.FileRepository;
import store.ckin.api.objectstorage.service.ObjectStorageService;
import store.ckin.api.tag.entity.Tag;
import store.ckin.api.tag.exception.TagNotFoundException;
import store.ckin.api.tag.repository.TagRepository;

/**
 * BookService 구현클래스.
 *
 * @author 나국로
 * @version 2024. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ObjectStorageService objectStorageService;
    private final FileRepository fileRepository;

    private static final String FILE_CATEGORY = "book";

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<BookListResponseDto> findByAuthorName(String authorName, Pageable pageable) {
        return bookRepository.findByAuthorName(authorName, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<BookListResponseDto> findByBookTitle(String bookTitle, Pageable pageable) {
        return bookRepository.findByBookTitleContaining(bookTitle, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<BookListResponseDto> findByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findByCategoryId(categoryId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<BookListResponseDto> findAllBooks(Pageable pageable) {
        return bookRepository.findAllBooks(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void createBook(BookCreateRequestDto requestDto, MultipartFile file) throws IOException {
        // Book 엔티티 생성
        Book book = Book.builder()
                .bookIsbn(requestDto.getBookIsbn())
                .bookTitle(requestDto.getBookTitle())
                .bookDescription(requestDto.getBookDescription())
                .bookPublisher(requestDto.getBookPublisher())
                .bookPublicationDate(requestDto.getBookPublicationDate())
                .bookIndex(requestDto.getBookIndex())
                .bookPackaging(requestDto.getBookPackaging())
                .bookState(requestDto.getBookState())
                .bookStock(requestDto.getBookStock())
                .bookRegularPrice(requestDto.getBookRegularPrice())
                .bookDiscountRate(requestDto.getBookDiscountRate())
                .bookSalePrice(calculateSalePrice(requestDto.getBookRegularPrice(), requestDto.getBookDiscountRate()))
                .authors(new HashSet<>())
                .categories(new HashSet<>())
                .tags(new HashSet<>())
                .build();


        // 각 작가에 대해 BookAuthor 연결 엔티티 생성 및 추가
        for (Long authorId : requestDto.getAuthorIds()) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AuthorNotFoundException(authorId));

            BookAuthor bookAuthor =
                    new BookAuthor(new BookAuthor.PK(book.getBookId(), author.getAuthorId()), book, author);
            book.getAuthors().add(bookAuthor);
        }

        // 카테고리 정보 연결
        for (Long categoryId : requestDto.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));
            BookCategory bookCategory =
                    new BookCategory(new BookCategory.PK(book.getBookId(), category.getCategoryId()), book, category);
            book.getCategories().add(bookCategory);
        }

        // 태그 정보 연결
        for (Long tagId : requestDto.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new TagNotFoundException(tagId));
            BookTag bookTag = new BookTag(new BookTag.PK(book.getBookId(), tag.getTagId()), book, tag);
            book.getTags().add(bookTag);
        }

        // Book 엔티티 저장 (연관된 BookAuthor 엔티티들도 함께 저장됨)
        bookRepository.save(book);
        File uploadFile = objectStorageService.saveFile(file, FILE_CATEGORY);
        File storeFile = uploadFile.toBuilder()
                .book(book)
                .build();
        fileRepository.save(storeFile);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void updateBook(Long bookId, BookModifyRequestDto requestDto) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Book updatedBook = existingBook.toBuilder()
                .bookIsbn(requestDto.getBookIsbn())
                .bookTitle(requestDto.getBookTitle())
                .bookDescription(requestDto.getBookDescription())
                .bookPublisher(requestDto.getBookPublisher())
                .bookPublicationDate(requestDto.getBookPublicationDate())
                .bookIndex(requestDto.getBookIndex())
                .bookPackaging(requestDto.getBookPackaging())
                .bookState(requestDto.getBookState())
                .bookStock(requestDto.getBookStock())
                .bookRegularPrice(requestDto.getBookRegularPrice())
                .bookDiscountRate(requestDto.getBookDiscountRate())
                .bookSalePrice(calculateSalePrice(requestDto.getBookRegularPrice(), requestDto.getBookDiscountRate()))
                .build();


        // 작가, 카테고리, 태그 정보 업데이트
        updateAuthors(updatedBook, requestDto.getAuthorIds());
        updateCategories(updatedBook, requestDto.getCategoryIds());
        updateTags(updatedBook, requestDto.getTagIds());

        bookRepository.save(updatedBook);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateBookThumbnail(Long bookId, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        File thumbnailFile = book.getThumbnail();
        File uploadFile = objectStorageService.saveFile(file, FILE_CATEGORY);

        File updateThumbnailFile = thumbnailFile.toBuilder()
                .fileOriginName(uploadFile.getFileOriginName())
                .fileUrl(uploadFile.getFileUrl())
                .build();
        fileRepository.save(updateThumbnailFile);
    }


    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public BookResponseDto findBookById(Long bookId) {
        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        return convertToBookResponseDto(book);
    }

    /**
     * {@inheritDoc}
     *
     * @param bookIds 도서 아이디 리스트
     * @return 도서 추출 정보 응답 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookExtractionResponseDto> getExtractBookListByBookIds(List<Long> bookIds) {
        return bookRepository.getExtractBookListByBookIds(bookIds);
    }

    @Override
    public List<BookMainPageResponseDto> getMainPageBookListByCategoryId(Long categoryId, Integer limit) {
        return bookRepository.getMainPageResponseDtoByCategoryId(categoryId,limit);
    }
    @Override
    public List<BookMainPageResponseDto> getMainPageBookListOrderByBookPublicationDate( Integer limit) {
        return bookRepository.getMainPageResponseDtoOrderByBookPublicationDate(limit);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> getBookCategoryIdsByBookIds(List<Long> bookIds) {
        return bookIds.stream()
                .map(bookId -> bookRepository.findByBookId(bookId)
                        .orElseThrow(() -> new BookNotFoundException(bookId)))
                .flatMap(book -> book.getCategories().stream())
                .map(bookCategory -> bookCategory.getCategory().getCategoryId())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }


    private void updateAuthors(Book book, Set<Long> authorIds) {
        book.getAuthors().clear();
        for (Long authorId : authorIds) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AuthorNotFoundException(authorId));
            book.getAuthors()
                    .add(new BookAuthor(new BookAuthor.PK(book.getBookId(), author.getAuthorId()), book, author));
        }
    }

    private void updateCategories(Book book, Set<Long> categoryIds) {
        book.getCategories().clear();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));
            book.getCategories()
                    .add(new BookCategory(new BookCategory.PK(book.getBookId(), category.getCategoryId()), book,
                            category));
        }
    }

    private void updateTags(Book book, Set<Long> tagIds) {
        book.getTags().clear();
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new TagNotFoundException(tagId));
            book.getTags().add(new BookTag(new BookTag.PK(book.getBookId(), tag.getTagId()), book, tag));
        }
    }


    private Integer calculateSalePrice(Integer regularPrice, Integer discountRate) {
        // 할인된 가격 계산 로직
        return regularPrice - (regularPrice * discountRate / 100);
    }

    private BookResponseDto convertToBookResponseDto(Book book) {
        List<String> authorNames = book.getAuthors().stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .collect(Collectors.toList());
        List<String> categoryNames = book.getCategories().stream()
                .map(bookCategory -> bookCategory.getCategory().getCategoryName())
                .collect(Collectors.toList());
        List<String> tagNames = book.getTags().stream()
                .map(bookTag -> bookTag.getTag().getTagName())
                .collect(Collectors.toList());
        String thumbnailUrl = book.getThumbnail() != null ? book.getThumbnail().getFileUrl() : null;

        return BookResponseDto.builder()
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
                .bookSalePrice(book.getBookSalePrice())
                .bookReviewRate(book.getBookReviewRate())
                .bookState(book.getBookState())
                .thumbnail(thumbnailUrl)
                .authorNames(authorNames)
                .categoryNames(categoryNames)
                .tagNames(tagNames)
                .build();
    }


}
