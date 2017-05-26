package org.neo4j.sdn.test;

import apoc.coll.Coll;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.service.Components;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.sdn.test.domain.User;
import org.neo4j.sdn.test.repository.UserRepository;
import org.neo4j.sdn.test.service.UserService;
import org.neo4j.shell.ShellSettings;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
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

    @Autowired
    private Session session;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void shouldBeAbleToCallProcedure() {
        Result result = session.query("RETURN apoc.coll.sum([0.5,1,2.5]) AS sum", emptyMap());
        assertThat(result.iterator().next().get("sum")).isEqualTo(4.0);
    }

    @Test
    public void useFulltextIndex() throws Exception {
        userRepository.save(new User("john.doe@example.com", "John", "Doe"));
        userRepository.save(new User("jane.doe@example.com", "Jane", "Doe"));

        List<User> johns = userRepository.findByName("name:John");
        assertThat(johns).hasSize(1);

        List<User> does = userRepository.findByName("name:Doe");
        assertThat(does).hasSize(2);
    }

    @Configuration
    @EnableNeo4jRepositories(basePackageClasses = UserRepository.class)
    @ComponentScan(basePackageClasses = UserService.class)
    static class Config {


        @Bean(destroyMethod = "shutdown")
        public GraphDatabaseService graphDatabaseService() {
            GraphDatabaseService graphDatabaseService = new TestGraphDatabaseFactory()
                .newImpermanentDatabaseBuilder()
//                .newEmbeddedDatabaseBuilder(new File("target/graph.db"))
                // Use GraphDatabaseSettings.* to set properties
                .setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
                .setConfig(GraphDatabaseSettings.node_keys_indexable, "name")
                .setConfig(GraphDatabaseSettings.forbid_shortestpath_common_nodes, "true")
                // Some properties might be elsewhere
                .setConfig(ShellSettings.remote_shell_enabled, "true")
                // Or load properties from file:
                // .loadPropertiesFromFile(this.getClass().getClassLoader().getResource("neo4j.properties").getPath())
                .newGraphDatabase();

            try (Transaction tx = graphDatabaseService.beginTx()) {
                Map<String, String> config = MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext");
                graphDatabaseService
                    .index()
                    .forNodes("node_auto_index", config);

                tx.success();
            }

            Procedures procedures = ((GraphDatabaseAPI) graphDatabaseService)
                .getDependencyResolver()
                .resolveDependency(Procedures.class);
            try {
                procedures.registerFunction(Coll.class);
            } catch (KernelException e) {
                throw new RuntimeException("Error registering", e);
            }

            return graphDatabaseService;
        }


        @Bean
        public SessionFactory getSessionFactory() {
            org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration();
            // Register your configuration here, this will confuse OGM so the driver you set below won't be destroyed
            Components.configure(configuration);

            // Register your driver
            EmbeddedDriver driver = new EmbeddedDriver(graphDatabaseService());
            Components.setDriver(driver);

            // Set driver class name so you won't get NPE
            configuration.driverConfiguration().setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");

            // Configure auto index
//            configuration.autoIndexConfiguration().setAutoIndex("assert");

            return new SessionFactory(configuration, User.class.getPackage().getName());
        }

        @Bean
        public Neo4jTransactionManager transactionManager() throws Exception {
            return new Neo4jTransactionManager(getSessionFactory());
        }


    }

    @Configuration
    @EnableNeo4jRepositories(basePackageClasses = UserRepository.class)
    @ComponentScan(basePackageClasses = UserService.class)
    static class EmbeddedConfig {


        @Bean(destroyMethod = "shutdown")
        public GraphDatabaseService graphDatabaseService() {
            GraphDatabaseService graphDatabaseService = new TestGraphDatabaseFactory()
                .newImpermanentDatabaseBuilder()
                .newGraphDatabase();

            Procedures procedures = ((GraphDatabaseAPI) graphDatabaseService)
                .getDependencyResolver()
                .resolveDependency(Procedures.class);

            try {
                procedures.registerFunction(Coll.class);
            } catch (KernelException e) {
                throw new RuntimeException("Error registering", e);
            }

            return graphDatabaseService;
        }

        @Bean
        public SessionFactory getSessionFactory() {
            org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration();
            EmbeddedDriver driver = new EmbeddedDriver(graphDatabaseService());
            Components.setDriver(driver);
            return new SessionFactory(configuration, User.class.getPackage().getName());
        }

        @Bean
        public Neo4jTransactionManager transactionManager() throws Exception {
            return new Neo4jTransactionManager(getSessionFactory());
        }
    }

}
