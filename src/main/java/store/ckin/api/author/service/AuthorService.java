package store.ckin.api.author.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import store.ckin.api.author.dto.request.AuthorCreateRequestDto;
import store.ckin.api.author.dto.request.AuthorModifyRequestDto;
import store.ckin.api.author.dto.response.AuthorResponseDto;

/**
 * AuthorService.
 *
 * @author 나국로
 * @version 2024. 02. 13.
 */
public interface AuthorService {
    /**
     * 모든 작가를 페이징하여 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 작가 목록에 대한 페이지 객체
     */
    Page<AuthorResponseDto> findAllAuthors(Pageable pageable);

    /**
     * 이름으로 작가를 검색하고 페이징된 결과를 반환합니다.
     *
     * @param name     작가의 이름
     * @param pageable 페이징 정보
     * @return 이름으로 검색된 작가 목록에 대한 페이지 객체
     */
    Page<AuthorResponseDto> findAuthorsByName(String name, Pageable pageable);

    /**
     * 새로운 작가를 생성하고 결과를 반환합니다.
     *
     * @param authorCreateRequestDto 작가 생성 요청 DTO
     * @return 생성된 작가에 대한 응답 DTO
     */
    AuthorResponseDto createAuthor(AuthorCreateRequestDto authorCreateRequestDto);

    /**
     * 특정 ID를 가진 작가의 정보를 조회합니다.
     *
     * @param authorId 작가의 ID
     * @return 작가에 대한 응답 DTO
     */
    AuthorResponseDto findAuthorById(Long authorId);

    /**
     * 특정 ID를 가진 작가의 정보를 수정하고 결과를 반환합니다.
     *
     * @param authorId               작가의 ID
     * @param authorModifyRequestDto 작가 수정 요청 DTO
     * @return 수정된 작가에 대한 응답 DTO
     */
    AuthorResponseDto updateAuthor(Long authorId, AuthorModifyRequestDto authorModifyRequestDto);

    /**
     * 특정 ID를 가진 작가를 삭제합니다.
     *
     * @param authorId 삭제할 작가의 ID
     */
    void deleteAuthor(Long authorId);

}
