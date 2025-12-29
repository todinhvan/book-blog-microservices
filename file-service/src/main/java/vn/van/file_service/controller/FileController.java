package vn.van.file_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.van.file_service.constant.ResponseMessage;
import vn.van.file_service.dto.response.ApiResponse;
import vn.van.file_service.dto.response.FileResponse;
import vn.van.file_service.service.FileService;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FileController {
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileResponse>> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("uploadFile");
        return ResponseEntity.ok(ApiResponse.<FileResponse>builder()
                .statusCode(ResponseMessage.FILE_UPLOAD_FAILED.getStatusCode())
                .status(ResponseMessage.FILE_UPLOAD_FAILED.getStatus())
                .message(ResponseMessage.FILE_UPLOAD_FAILED.getMessage())
                .data(fileService.upload(file))
                .build());
    }
}
