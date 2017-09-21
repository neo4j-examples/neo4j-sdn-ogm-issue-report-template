package org.neo4j.sdn.test.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
public class User {

    @GraphId
    private Long id;

    private String firstName;

    private String lastName;

    @Index(unique = true)
    private String email;

    @Relationship(type = "FRIEND_OF", direction = INCOMING)
    private Set<User> friends;

    public User() {
        // required by
    }

    public User(String email, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Relationship(type = "FRIEND_OF", direction = INCOMING)
    public Set<User> getFriends() {
        return friends;
    }

    @Relationship(type = "FRIEND_OF", direction = INCOMING)
    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
}
