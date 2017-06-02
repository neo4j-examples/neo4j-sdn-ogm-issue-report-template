package org.neo4j.sdn.test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.service.Components;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.sdn.test.domain.User;
import org.neo4j.sdn.test.repository.UserRepository;
import org.neo4j.sdn.test.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

/**
 * @author Frantisek Hartman
 */
@Configuration
@EnableNeo4jRepositories(basePackageClasses = UserRepository.class, enableDefaultTransactions = false)
@ComponentScan(basePackageClasses = UserService.class)
class Config {

    static {
        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration();
        configuration.driverConfiguration()
                     .setDriverClassName(EmbeddedDriver.class.getName());

        Components.configure(configuration);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory(User.class.getPackage().getName());
    }

    @Bean
    public Neo4jTransactionManager transactionManager() throws Exception {
        return new Neo4jTransactionManager(getSessionFactory());
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        EmbeddedDriver driver = (EmbeddedDriver) Components.driver();
        return driver.getGraphDatabaseService();
    }

   /* @Bean
    public org.neo4j.ogm.config.Configuration configuration() {

//                     .setURI(SdnTestCase.neoServer.boltURI().toString());
        // use this for HTTP driver
        //					.setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
        //					.setURI(neoServer.boltURI().toString());

        return configuration;
    }*/
}
