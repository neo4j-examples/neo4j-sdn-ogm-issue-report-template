package org.neo4j.sdn.test.repository;

import org.neo4j.sdn.test.domain.User;
import org.neo4j.sdn.test.domain.UserHolder.UserHolder;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends Neo4jRepository<User, Long> {

    @Query("MATCH (u:User) "
        + "WHERE u.email = {email} "
        + "OPTIONAL MATCH (u)<-[f:FRIEND_OF]-(friend) "
        + "RETURN u, collect(f), collect(friend)")
    User findByEmail(@Param("email") String email);

    @Query("MATCH (u:User) "
        + "WHERE u.email = {email} "
        + "OPTIONAL MATCH (u)<-[f:FRIEND_OF]-(friend) "
        + "RETURN u, collect(f), collect(friend)")
    UserHolder findByEmailReturnHolder(@Param("email") String email);

}
