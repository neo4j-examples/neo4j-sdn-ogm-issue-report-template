package org.neo4j.ogm.test.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author Frantisek Hartman
 */
@RelationshipEntity(type = "ACCEPTED")
public class AcceptedRel {

    private Long id;

    @StartNode
    private Nurse nurse;

    @EndNode
    private TimeSlot timeSlot;

    public AcceptedRel() {
    }

    public AcceptedRel(Nurse nurse, TimeSlot timeSlot) {
        this.nurse = nurse;
        this.timeSlot = timeSlot;

        nurse.addAcceptedRel(this);
        timeSlot.addAcceptedRel(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}
