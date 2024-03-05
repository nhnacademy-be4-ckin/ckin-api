package store.ckin.api.file.exception;

/**
 * {class name}.
 *
 * @author 나국로
 * @version 2024. 03. 03.
 */
public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String fileUrl) {
        super(String.format("파일을 찾을 수 없습니다: %s", fileUrl));
    }
}