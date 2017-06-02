package org.neo4j.sdn.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Frantisek Hartman
 */
@Configuration
public class AnotherConfig {

    @Bean
    public String someBean() {
        return "this will cause a new Spring context to be created";
    }
}
