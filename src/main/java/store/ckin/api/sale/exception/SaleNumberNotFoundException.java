package store.ckin.api.sale.exception;

/**
 * 주문번호로 조회된 주문이 없는 경우에 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 09.
 */

public class SaleNumberNotFoundException extends RuntimeException {

    public SaleNumberNotFoundException(String saleNumber) {
        super(String.format("주문 정보가 존재하지 않습니다. (주문번호 = %s)", saleNumber));
    }
}
