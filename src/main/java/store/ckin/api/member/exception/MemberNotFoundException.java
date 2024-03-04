package store.ckin.api.member.exception;

/**
 * 정보에 일치하는 Member 정보가 없을 때 호출 되는 Exception 입니다.
 *
 * @author : jinwoolee
 * @version : 2024. 02. 20.
 */
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String email) {
        super(String.format("Member not found for this email [%s]", email));
    }
}
