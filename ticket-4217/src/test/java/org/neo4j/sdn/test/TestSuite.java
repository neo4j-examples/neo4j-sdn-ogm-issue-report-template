package org.neo4j.sdn.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Frantisek Hartman
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
    AnotherTest.class,
    SdnTestCase.class,
    AnotherTest.class,
    SdnTestCase.class
})
public class TestSuite {
}
