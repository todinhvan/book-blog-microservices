package vn.van.file_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.van.file_service.dto.response.FileInfo;
import vn.van.file_service.entity.FileManagement;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(target = "id", source = "name")
    FileManagement toFileManagement(FileInfo fileInfo);
}
