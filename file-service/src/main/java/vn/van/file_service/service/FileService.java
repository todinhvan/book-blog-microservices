package vn.van.file_service.service;

import org.springframework.web.multipart.MultipartFile;
import vn.van.file_service.dto.response.FileResponse;

public interface FileService {
    FileResponse upload(MultipartFile file);
}
