package store.ckin.api.pointhistory.exception;

/**
 * 포인트 내역을 찾을 수 없을 때 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 19.
 */

public class PointHistoryNotFoundException extends RuntimeException {
    public PointHistoryNotFoundException() {
        super("포인트 내역을 찾을 수 없습니다.");
    }
}
