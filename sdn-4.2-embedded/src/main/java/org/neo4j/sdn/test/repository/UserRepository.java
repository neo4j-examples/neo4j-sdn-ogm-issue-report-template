package org.neo4j.sdn.test.repository;

import org.neo4j.sdn.test.domain.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByLastNameLike(String lastName);

    @Query("START n=node:node_auto_index({name}) RETURN n")
    List<User> findByName(@Param("name") String name);
}
