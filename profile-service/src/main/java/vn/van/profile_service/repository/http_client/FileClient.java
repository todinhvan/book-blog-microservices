package vn.van.profile_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import vn.van.profile_service.dto.response.ApiResponse;
import vn.van.profile_service.dto.response.FileResponse;

@FeignClient(name = "file-service", url = "${app.services.file}")
public interface FileClient {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadFile(@RequestPart("file") MultipartFile file);

}
