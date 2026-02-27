package vn.van.file_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.van.file_service.constant.ResponseMessage;
import vn.van.file_service.dto.response.ApiResponse;
import vn.van.file_service.dto.response.FileData;
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

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        log.info("downloadFile");
        FileData fileData = fileService.download(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
//                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate")
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"") // attachment: download trực tiếp khi truy cập
                .body(fileData.resource());
    }
}
