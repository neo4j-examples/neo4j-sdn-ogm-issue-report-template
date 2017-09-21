package org.neo4j.sdn.test;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.sdn.test.domain.User;
import org.neo4j.sdn.test.domain.UserHolder.UserHolder;
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

import org.neo4j.ogm.session.SessionFactory;

import static com.google.common.collect.Sets.newHashSet;
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

    @Test(expected = RuntimeException.class)
    public void findByEmailReturnUser() {
        User user1 = new User("1@example.com", "one", "example");
        User user2 = new User("2@example.com", "two", "example");
        User user3 = new User("3@example.com", "three", "example");

        user1.setFriends(newHashSet(user2, user3));
        User saved = userRepository.save(user1);

        // this fails, no single result matching type user
        User loaded = userRepository.findByEmail(user1.getEmail());
    }


    @Test
    public void findByEmailReturnHolder() {
        User user1 = new User("1@example.com", "one", "example");
        User user2 = new User("2@example.com", "two", "example");
        User user3 = new User("3@example.com", "three", "example");

        user1.setFriends(newHashSet(user2, user3));
        userRepository.save(user1);

        UserHolder loaded = userRepository.findByEmailReturnHolder(user1.getEmail());

        assertThat(loaded).isNotNull();
        assertThat(loaded.u).isNotNull();
        assertThat(loaded.u.getFriends()).hasSize(2);
    }

    @Configuration
    @EnableNeo4jRepositories(basePackageClasses = UserRepository.class)
    @ComponentScan(basePackageClasses = UserService.class)
    static class Config {

        @Bean
        public SessionFactory getSessionFactory() {
            return new SessionFactory(configuration(), User.class.getPackage().getName());
        }

        @Bean
        public Neo4jTransactionManager transactionManager() throws Exception {
            return new Neo4jTransactionManager(getSessionFactory());
        }

        @Bean
        public org.neo4j.ogm.config.Configuration configuration() {
            org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration();
            configuration.driverConfiguration()
                    .setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
                    .setURI(neoServer.boltURI().toString());
// use this for HTTP driver
//					.setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
//					.setURI(neoServer.boltURI().toString());

            return configuration;
        }
    }
}
