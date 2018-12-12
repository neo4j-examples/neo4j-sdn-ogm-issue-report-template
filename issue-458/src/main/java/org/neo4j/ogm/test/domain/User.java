package org.neo4j.ogm.test.domain;

//import org.neo4j.ogm.annotation.NodeEntity;
//
// When @NodeEntity is commented out below, with OGM v3.0.2 OgmTestCase will fail and produce the following warning:
// "Unable to find database label for entity org.neo4j.ogm3.test.domain.User : no results will be returned.
//  Make sure the class is registered, and not abstract without @NodeEntity annotation"
//
//@NodeEntity
public interface User {

  String getEmail();

  String getFirstName();

  String getLastName();

}
