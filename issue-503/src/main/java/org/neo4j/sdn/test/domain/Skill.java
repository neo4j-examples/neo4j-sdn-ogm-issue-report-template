package org.neo4j.sdn.test.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.Objects;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

/**
 * Class Skill.
 *
 * @author pyec6553
 */
@NodeEntity
public class Skill {
    @Id
    @GeneratedValue
    Long id;

    @Index(primary = true)
    @Property
    String name;

    @Relationship(type = "HAS_SKILL", direction = INCOMING)
    List<Skilled> skilled;

    public Skill(String name) {
        this.name = name;
    }

    public Skill() { }

    public String getName() {
        return name;
    }

    public List<Skilled> getSkilled() {
        return skilled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill)) return false;
        Skill skill = (Skill) o;
        return Objects.equals(name, skill.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
