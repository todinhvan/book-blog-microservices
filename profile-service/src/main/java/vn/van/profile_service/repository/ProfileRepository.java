package vn.van.profile_service.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import vn.van.profile_service.entity.Profile;

import java.util.List;

@Repository
public interface ProfileRepository extends Neo4jRepository<Profile, String> {
    List<Profile> findAllByUserId(String userId);
}
