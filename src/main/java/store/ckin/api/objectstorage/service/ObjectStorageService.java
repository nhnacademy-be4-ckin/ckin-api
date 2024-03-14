package store.ckin.api.objectstorage.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import store.ckin.api.file.entity.File;

/**
 * ObjectStorageService 인터페이스.
 *
 * @author 나국로
 * @version 2024. 03. 01.
 */
public interface ObjectStorageService {
    File saveFile(MultipartFile file, String category) throws IOException;

    void deleteFile(String url);
}
