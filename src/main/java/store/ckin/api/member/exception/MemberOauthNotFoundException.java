package store.ckin.api.member.exception;

import store.ckin.api.advice.exception.GeneralNotFoundException;

/**
 * {class name}.
 *
 * @author 정승조
 * @version 2024. 03. 22.
 */
public class MemberOauthNotFoundException extends GeneralNotFoundException {
    public MemberOauthNotFoundException(String oauthId) {
        super(String.format("Member Oauth not found for this oauthId [%s]", oauthId));
    }
}
