package store.ckin.api.sale.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import store.ckin.api.common.dto.PagedResponse;
import store.ckin.api.sale.dto.response.SaleCheckResponseDto;
import store.ckin.api.sale.dto.response.SaleInfoResponseDto;
import store.ckin.api.sale.dto.response.SaleResponseDto;
import store.ckin.api.sale.dto.response.SaleWithBookResponseDto;

/**
 * 주문 Repository Custom 인터페이스입니다.
 *
 * @author 정승조
 * @version 2024. 03. 03.
 */

@NoRepositoryBean
public interface SaleRepositoryCustom {

    /**
     * 주문 상세 정보를 조회하는 메서드.
     *
     * @param saleId 주문 ID
     * @return 주문 응답 DTO
     */
    SaleResponseDto findBySaleId(Long saleId);

    /**
     * 주문 번호로 주문 상세 정보와 주문한 책 정보를 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호
     * @return 주문 상세 정보와 주문한 책 정보 DTO
     */
    SaleWithBookResponseDto getSaleWithBook(String saleNumber);


    /**
     * 주문 번호로 주문을 조회하는 메서드입니다.
     *
     * @param saleNumber 주문 번호 (UUID)
     * @return 주문 응답 DTO
     */
    SaleResponseDto findBySaleNumber(String saleNumber);

    /**
     * 회원 ID로 전체 주문 정보를 조회하는 메서드입니다.
     *
     * @param memberId 회원 ID
     * @param pageable 페이지 정보
     * @return 주문 응답 DTO 리스트
     */
    PagedResponse<List<SaleInfoResponseDto>> findAllByMemberId(Long memberId, Pageable pageable);

    /**
     * 회원 ID와 도서 ID로 주문을 조회하는 메서드입니다.
     *
     * @param memberId 회원 ID
     * @param bookId   도서 ID
     * @return 주문 체크 응답 DTO
     */
    SaleCheckResponseDto checkSaleByMemberIdAndBookId(Long memberId, Long bookId);
}
