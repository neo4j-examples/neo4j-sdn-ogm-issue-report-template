package org.neo4j.sdn.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.sdn.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Frantisek Hartman
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
    AnotherConfig.class,
    Config.class
})
@Transactional
public class AnotherTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GraphDatabaseService graphDatabaseService;

    @Test
    public void name() throws Exception {
        graphDatabaseService.execute("MATCH (n) RETURN count(n)");
    }
}
