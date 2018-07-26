package org.neo4j.sdn.test.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "firstName")
    private String firstName;

    @Property(name = "lastName")
    private String lastName;

    @Index(unique = true)
    private String email;

    @Relationship(type = "HAS_SKILL")
    private List<Skilled> relationships;

    public User() {}

    public User(String email, String firstName, String lastName, List<Skilled> relationships) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.relationships = relationships;
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

    public List<Skilled> getRelationships() {
        return relationships;
    }

    public User setRelationships(List<Skilled> relationships) {
        this.relationships = relationships;
        return this;
    }
}
