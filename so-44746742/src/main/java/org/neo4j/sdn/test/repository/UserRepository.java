package org.neo4j.sdn.test.repository;

import org.neo4j.sdn.test.domain.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, Long> {

    @Query("MATCH (u:User) WHERE ID(u) = {userId} SET u._lock = true RETURN u")
    User acquireLock(@Param("userId") Long userId);

    @Query("MATCH (u:User) WHERE ID(u) = {userId} SET u._lock = false")
    void releaseLock(@Param("userId") Long userId);

    User findByEmail(String email);

    List<User> findByLastNameLike(String lastName);
}
