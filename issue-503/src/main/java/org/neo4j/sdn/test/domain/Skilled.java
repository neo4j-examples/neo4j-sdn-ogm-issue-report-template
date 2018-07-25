package org.neo4j.sdn.test.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Objects;

/**
 * Class Skilled.
 *
 * @author pyec6553
 */
@RelationshipEntity(type = "HAS_SKILL")
public class Skilled {
    @Id
    @GeneratedValue
    Long id;

    @Property
    Long since;

    @StartNode
    User user;

    @EndNode
    Skill skill;

    public Skilled() {
    }

    public Skilled(Long id, Long since, User user, Skill skill) {
        this.id = id;
        this.since = since;
        this.user = user;
        this.skill = skill;
    }

    public User getUser() {
        return user;
    }

    public Skill getSkill() {
        return skill;
    }

    public Long getSince() {
        return since;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skilled)) return false;
        Skilled skilled = (Skilled) o;
        return Objects.equals(since, skilled.since);
    }

    @Override
    public int hashCode() {

        return Objects.hash(since);
    }
}
