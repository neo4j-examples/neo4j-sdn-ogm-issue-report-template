package org.neo4j.ogm.test;

import java.io.File;
import java.util.Collection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.test.domain.User;
import org.neo4j.ogm.test.domain.UserImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class OgmTestCase {

  @Rule
  public Neo4jRule neoServer = new Neo4jRule();
  private Session session;

  @Before
  public void setUp() throws Exception {

    Configuration configuration = new Configuration.Builder()
            .uri(new File("target/graph.db").toURI().toString()) // For Embedded
            .build();

    SessionFactory sessionFactory = new SessionFactory(configuration, User.class.getPackage().getName());
    session = sessionFactory.openSession();
    session.purgeDatabase();
  }

  @Test
  public void sampleTest() {
    User user = new UserImpl("noone@nowhere.com", "No", "One");
    session.save(user);

    Collection<User> allUsers = session.loadAll(User.class);
    assertThat(allUsers).hasSize(1);

    assertThat(allUsers.iterator().next().getEmail()).isEqualTo("noone@nowhere.com");
  }
}
