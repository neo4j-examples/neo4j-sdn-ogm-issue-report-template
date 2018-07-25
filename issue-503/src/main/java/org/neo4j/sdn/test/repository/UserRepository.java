package org.neo4j.sdn.test.repository;

import org.neo4j.sdn.test.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {

    User findByEmail(String email);

    Collection<User> findByLastNameLike(String lastName);
}
