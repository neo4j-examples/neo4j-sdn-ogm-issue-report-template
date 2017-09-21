package org.neo4j.sdn.test.domain.UserHolder;

import org.neo4j.sdn.test.domain.User;
import org.springframework.data.neo4j.annotation.QueryResult;

/**
 * @author Frantisek Hartman
 */
@QueryResult
public class UserHolder {

    public User u;
}
