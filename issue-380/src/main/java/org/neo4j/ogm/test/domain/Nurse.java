package org.neo4j.ogm.test.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Frantisek Hartman
 */
public class Nurse extends NeoEntity {


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
