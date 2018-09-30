package org.neo4j.ogm.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.test.domain.Person;
import org.neo4j.ogm.test.domain.User;

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

        SessionFactory sessionFactory = new SessionFactory(configuration, User.class.getPackage().getName());
        session = sessionFactory.openSession();
        session.purgeDatabase();
    }
    
    @Test
    public void unidirectionalRelationshipTest() {
        
    	Person dave = new Person("Dave");
    	Person mike = new Person("Mike");
        
        dave.addFriend(mike);
        
        session.save(dave);

        // Assert that the relationship is unidirectional from Dave -> Mike and not Dave <- Mike
    	String cypherRelationshipCount = "match (n)<-[r:FRIEND_OF]-(m) return count(r) as relationshipCount";
        Result result = session.query(cypherRelationshipCount, new HashMap<String, Object>());
        long relationshipCount = (long) result.iterator().next().get("relationshipCount");
        assertThat(relationshipCount == 0);        
        cypherRelationshipCount = "match (n)-[r:FRIEND_OF]->(m) return count(r) as relationshipCount";
        result = session.query(cypherRelationshipCount, new HashMap<String, Object>());
        relationshipCount = (long) result.iterator().next().get("relationshipCount");
        assertThat(relationshipCount == 1);

        session.clear();
        
        Person mikeLoaded = session.load(Person.class, mike.getId(), 2);
        session.save(mikeLoaded);
        
        // Assert that the relationship is unidirectional from Dave -> Mike and not Dave <- Mike
    	cypherRelationshipCount = "match (n)<-[r:FRIEND_OF]-(m) return count(r) as relationshipCount";
        result = session.query(cypherRelationshipCount, new HashMap<String, Object>());
        relationshipCount = (long) result.iterator().next().get("relationshipCount");
        assertThat(relationshipCount == 0); // NOTE: The behavior described by zoetsekas would result in a count of 1       
        cypherRelationshipCount = "match (n)-[r:FRIEND_OF]->(m) return count(r) as relationshipCount";
        result = session.query(cypherRelationshipCount, new HashMap<String, Object>());
        relationshipCount = (long) result.iterator().next().get("relationshipCount");
        assertThat(relationshipCount == 1);        

    }
    
}
