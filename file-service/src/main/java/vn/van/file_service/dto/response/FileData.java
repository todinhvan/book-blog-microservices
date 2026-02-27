package vn.van.file_service.dto.response;

import org.springframework.core.io.Resource;

// Record tương tự như FileResponse. Mới Java 21
// Nhưng nó có tính chất immutable (không có quyền set lại value sau khi init)
public record FileData(String contentType, Resource resource) { }
