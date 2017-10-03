package org.neo4j.boot.test.repository;

import org.neo4j.boot.test.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {

}
