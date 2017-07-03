package org.neo4j.ogm.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.test.domain.AcceptedRel;
import org.neo4j.ogm.test.domain.NeoEntity;
import org.neo4j.ogm.test.domain.Nurse;
import org.neo4j.ogm.test.domain.TimeSlot;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class OgmTestCase {

    @Rule
    public Neo4jRule neoServer = new Neo4jRule();
    private Session session;

    @Before
    public void setUp() throws Exception {

        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration();
        configuration.driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
                .setURI(neoServer.boltURI().toString());
//                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
//                .setURI(neoServer.httpURI().toString());

        SessionFactory sessionFactory = new SessionFactory(configuration, NeoEntity.class.getPackage().getName());
        session = sessionFactory.openSession();
        session.purgeDatabase();
    }

    @Test
    public void sampleTest() {

        Nurse nurse = new Nurse();
        TimeSlot timeSlot = new TimeSlot();

        AcceptedRel acceptedRel = new AcceptedRel(nurse, timeSlot); // also adds new rel to nurse and timeSlot

        session.save(nurse);
        session.clear();

        Nurse loadedNurse = session.load(Nurse.class, nurse.getId());
        assertThat(loadedNurse.getAccepted()).hasSize(1);
        TimeSlot otherTimeSlot = loadedNurse.getAccepted().iterator().next().getTimeSlot();
        assertThat(otherTimeSlot.getAccepted()).hasSize(1);

        session.clear();

        TimeSlot loadedTimeSlot = session.load(TimeSlot.class, timeSlot.getId());
        assertThat(loadedTimeSlot.getAccepted()).hasSize(1);
        Nurse otherNurse = loadedTimeSlot.getAccepted().iterator().next().getNurse();
        assertThat(otherNurse.getAccepted()).hasSize(1);

    }
}
