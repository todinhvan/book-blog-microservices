package vn.van.file_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.van.file_service.constant.ResponseMessage;
import vn.van.file_service.dto.response.FileData;
import vn.van.file_service.dto.response.FileInfo;
import vn.van.file_service.dto.response.FileResponse;
import vn.van.file_service.entity.FileManagement;
import vn.van.file_service.exception.ApplicationException;
import vn.van.file_service.mapper.FileMapper;
import vn.van.file_service.repository.FileManagementRepository;
import vn.van.file_service.repository.FileRepository;
import vn.van.file_service.service.FileService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileServiceImpl implements FileService {
    FileRepository fileRepository;
    FileManagementRepository fileManagementRepository;
    FileMapper fileMapper;

    @Override
    public FileResponse upload(MultipartFile file) {
        try {
            FileInfo info = fileRepository.store(file);
            FileManagement fileManagement = fileMapper.toFileManagement(info);
            fileManagement.setOwnerId(extractUserId());

            fileManagementRepository.save(fileManagement);
            return FileResponse.builder()
                    .originalFileName(file.getOriginalFilename())
                    .url(info.getUrl())
                    .build();

        } catch (IOException e) {
            throw new ApplicationException(ResponseMessage.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public FileData download(String fileName) {
        FileManagement fileManagement = fileManagementRepository.findById(fileName)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.FILE_NOT_FOUND));
        try {
            return new FileData(fileManagement.getContentType(), fileRepository.read(fileManagement));
        } catch (IOException e) {
            throw new ApplicationException(ResponseMessage.INVALID_FILE);
        }
    }

    private String extractUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("user-id");
    }
}
