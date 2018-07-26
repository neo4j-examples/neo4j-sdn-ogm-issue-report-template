package org.neo4j.sdn.test;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;
import org.neo4j.sdn.test.domain.Skill;
import org.neo4j.sdn.test.domain.Skilled;
import org.neo4j.sdn.test.domain.User;
import org.neo4j.sdn.test.repository.UserRepository;
import org.neo4j.sdn.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SdnTestCase.Config.class)
@EnableTransactionManagement
public class SdnTestCase {

    @ClassRule
    public static Neo4jRule neoServer = new Neo4jRule();

    @Autowired
    private UserService userService;

    @Autowired
    private SessionFactory sessionFactory;

    private Session session;

    @Before
    public void setUp() throws Exception {
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() throws Exception {
        session.purgeDatabase();
    }

    @Test
    public  void okWithSession() {
        Skill skill = new Skill("None");
        User user = new User("noone@nowhere.com", "No", "One", Collections.emptyList());
        Skilled relationship = new Skilled(null, 2018L, user, skill);
        user.setRelationships(Collections.singletonList(relationship));


        session.save(user);
        Collection<User> users = session.loadAll(User.class);
        assertThat(users.iterator().next().getRelationships()).isNotNull();
    }

    @Test
    public  void failWithRepository() {
        Skill skill = new Skill("None");
        User user = new User("noone@nowhere.com", "No", "One", Collections.emptyList());
        Skilled relationship = new Skilled(null, 2018L, user, skill);
        user.setRelationships(Collections.singletonList(relationship));

        Session session = sessionFactory.openSession();
        session.save(user);
        assertThat(userService.findAllUsers()).hasSize(1);
        assertThat(userService.findAllUsers().iterator().next().getRelationships()).isNotNull();
        assertEquals(relationship, userService.findAllUsers().iterator().next().getRelationships().get(0));
        session.purgeDatabase();
    }



    @Configuration
    @EnableNeo4jRepositories(basePackageClasses = UserRepository.class)
    @ComponentScan(basePackageClasses = UserService.class)
    static class Config {

        @Bean
        public SessionFactory sessionFactory() {
            return new SessionFactory(configuration(), User.class.getPackage().getName());
        }

        @Bean
        public Neo4jTransactionManager transactionManager() throws Exception {
            return new Neo4jTransactionManager(sessionFactory());
        }

        @Bean
        public org.neo4j.ogm.config.Configuration configuration() {
            return new org.neo4j.ogm.config.Configuration.Builder()
                    .uri(neoServer.boltURI().toString())
                    .build();
// use this for HTTP driver
//                    .uri(neoServer.httpURI().toString())
//                    .build();
        }
    }
}
