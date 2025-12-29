package vn.van.file_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.van.file_service.entity.FileManagement;

@Repository
public interface FileManagementRepository extends MongoRepository<FileManagement, String> {
}
