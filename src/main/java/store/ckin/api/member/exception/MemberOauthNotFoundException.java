package store.ckin.api.member.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * Member Oauth 를 찾지 못할 때 발생하는 예외입니다.
 *
 * @author 정승조
 * @version 2024. 03. 22.
 */
public class MemberOauthNotFoundException extends GeneralNotFoundException {
    public MemberOauthNotFoundException(String oauthId) {
        super(String.format("회원 Oauth ID를 찾을 수 없습니다. [oauthId = %s]", oauthId));
    }
}
