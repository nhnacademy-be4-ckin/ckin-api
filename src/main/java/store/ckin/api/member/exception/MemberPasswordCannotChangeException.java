package store.ckin.api.member.exception;

/**
 * 비밀번호를 바꿀수 없을 때 호출되는 메서드 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 03. 24.
 */
public class MemberPasswordCannotChangeException extends RuntimeException {
    public MemberPasswordCannotChangeException(Long memberId) {
        super(String.format("비밀번호를 바꿀 수 없습니다. ID : [%d]", memberId));
    }
}
