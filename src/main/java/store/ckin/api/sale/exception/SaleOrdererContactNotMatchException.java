package store.ckin.api.sale.exception;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 14.
 */
public class SaleOrdererContactNotMatchException extends RuntimeException {

    public SaleOrdererContactNotMatchException(String saleNumber, String ordererContact) {
        super(String.format("주문 번호 (%s)의 주문자 연락처 (%s)가 일치하지 않습니다.", saleNumber, ordererContact));
    }
}
