package store.ckin.api.tag.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.error.ErrorResponse;
import store.ckin.api.tag.dto.request.TagCreateRequestDto;
import store.ckin.api.tag.dto.request.TagDeleteRequestDto;
import store.ckin.api.tag.dto.request.TagUpdateRequestDto;
import store.ckin.api.tag.dto.response.TagResponseDto;
import store.ckin.api.tag.exception.TagNameAlreadyExistException;
import store.ckin.api.tag.exception.TagNotFoundException;
import store.ckin.api.tag.service.TagService;

/**
 * 태그 요청을 받는 컨트롤러 클래스
 *
 * @author : 김준현
 * @version : 2024. 02. 13
 */

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * 모든 태그 리스트를 가져오는 컨트롤러 메서드
     *
     * @return 모든 태그 리스트
     */
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAllTagList() {
        return ResponseEntity.ok(tagService.readTagList());
    }

    /**
     * 태그를 저장하는 컨트롤러 메서드
     * @param tagCreateRequestDto 태그 생성 요청 Dto
     * @return 성공시 CREATED, 태그 이름 이미 존재할 시 BAD_REQUEST, Validation Fail 시 BAD_REQUEST
     */
    @PostMapping
    public ResponseEntity<Void> saveTag(@Valid @RequestBody TagCreateRequestDto tagCreateRequestDto) {
        tagService.createTag(tagCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 태그를 수정하는 컨트롤러 메서드
     * @param tagUpdateRequestDto 태그 수정 요청 Dto
     * @return 성공 시 Ok, 존재하지 않는 태그 수정 요청시 NOT_FOUND
     */
    @PutMapping
    public ResponseEntity<Void> updateTag(@Valid @RequestBody TagUpdateRequestDto tagUpdateRequestDto) {
        tagService.updateTag(tagUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 태그를 삭제하는 컨트롤러 메서드
     * @param tagDeleteRequestDto 태그 삭제 요청 Dto
     * @return 성공 시 Ok, 존재하지 않는 태그 삭제 요청시 NOT_FOUND
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@Valid @RequestBody TagDeleteRequestDto tagDeleteRequestDto) {
        tagService.deleteTag(tagDeleteRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 이미 존재하는 태그 이름일 때 발생하는 TagNameAlreadyExistException 핸들링 메서드
     *
     * @param e 태그 이름이 이미 존재할 때 발생
     * @return BAD REQUEST 상태와 code, message 를 담은 JSON 응답
     */
    @ExceptionHandler(TagNameAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleTagAlreadyExistException(TagNameAlreadyExistException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("TagName Already Exist")
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 존재하지 않는 태그 아이디일 때 발생하는 TagNotFoundException 핸들링 메서드
     * @param e 태그 아이디가 존재하지 않을 때 발생
     * @return NOT_FOUND 상태와 코드, 메세지를 담은 JSON 응답
     */
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTagNotFoundException(TagNotFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("Tag Not Found")
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
