package org.neo4j.ogm.test.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * @author Frantisek Hartman
 */
@NodeEntity
public class TimeSlot extends NeoEntity {

    @Relationship(type = "ACCEPTED", direction = "INCOMING")
    Set<AcceptedRel> accepted;

    public Set<AcceptedRel> getAccepted() {
        return accepted;
    }

    void addAcceptedRel(AcceptedRel acceptedRel) {
        if (accepted == null) {
            accepted = new HashSet<>();
        }
        accepted.add(acceptedRel);
    }
}
