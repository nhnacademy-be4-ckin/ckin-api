package store.ckin.api.sale.exception;

/**
 * 주문 번호로 주문을 조회할 때 주문이 존재하지 않는 경우 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 13.
 */
public class SaleNotFoundExceptionBySaleNumber extends RuntimeException {

    public SaleNotFoundExceptionBySaleNumber(String number) {
        super(String.format("주문 번호(%s)에 해당하는 주문이 존재하지 않습니다.", number));
    }
}
