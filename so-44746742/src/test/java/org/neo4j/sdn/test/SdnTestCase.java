package org.neo4j.sdn.test;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.sdn.test.domain.User;
import org.neo4j.sdn.test.repository.UserRepository;
import org.neo4j.sdn.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SdnTestCase.Config.class)
public class SdnTestCase {

    private static final Logger logger = LoggerFactory.getLogger(SdnTestCase.class);

    @ClassRule
    public static Neo4jRule neoServer = new Neo4jRule();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Naive counter - classic example of a lost update
     */
    @Test
    public void naiveCounter() throws InterruptedException {
        User user = new User("noone@nowhere.com", "No", "One");
        user.setCounter(0);

        userRepository.save(user);

        int count = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                userService.incrementCounter(user.getId());
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        User loaded = userRepository.findOne(user.getId());
        assertThat(loaded.getCounter()).isEqualTo(count);
    }

    /**
     * Synchronized counter - usable only within one JVM, may be faster because of network overhead when acquiring lock
     */
    @Test
    public void synchronizedCounter() throws InterruptedException {
        User user = new User("noone@nowhere.com", "No", "One");
        user.setCounter(0);

        userRepository.save(user);

        int count = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                synchronized (userService) {
                    userService.incrementCounter(user.getId());
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        User loaded = userRepository.findOne(user.getId());
        assertThat(loaded.getCounter()).isEqualTo(count);
    }

    /**
     * Using database write lock to synchronize - will work for multiple clients, which can't be synchronized
     */
    @Test
    public void counterWithLock() throws InterruptedException {
        User user = new User("noone@nowhere.com", "No", "One");
        user.setCounter(0);

        userRepository.save(user);

        int count = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {

                    userService.incrementCounterWithLock(user.getId());
                } catch (Exception e) {
                    logger.info("Exception", e);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        User loaded = userRepository.findOne(user.getId());
        assertThat(loaded.getCounter()).isEqualTo(count);
    }


    @Configuration
    @EnableNeo4jRepositories(basePackageClasses = UserRepository.class)
    @EnableTransactionManagement
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
