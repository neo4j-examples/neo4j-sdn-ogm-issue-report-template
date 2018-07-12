package org.neo4j.sdn.test;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.session.SessionFactory;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SdnTestCase.Config.class)
@EnableTransactionManagement
public class SdnTestCase {

    @ClassRule
    public static Neo4jRule neoServer = new Neo4jRule();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void sampleTest() {
        User user = new User("noone@nowhere.com", "No", "One");
        User saved = userRepository.save(user);

        assertThat(saved).isNotNull();

        assertThat(userRepository.findByEmail("noone@nowhere.com")).isNotNull();
        assertThat(userService.findAllUsers()).hasSize(1);
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
