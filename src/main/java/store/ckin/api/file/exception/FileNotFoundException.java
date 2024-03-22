package store.ckin.api.file.exception;

/**
 * 파일을 찾을 수 없을 때 호출되는 Exception 입니다.
 *
 * @author 나국로
 * @version 2024. 03. 03.
 */
public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String fileUrl) {
        super(String.format("파일을 찾을 수 없습니다. [fileUrl = %s]", fileUrl));
    }
}