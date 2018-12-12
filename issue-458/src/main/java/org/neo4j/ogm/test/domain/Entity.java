package org.neo4j.ogm.test.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

class Entity {

  @Id @GeneratedValue
  private Long id;

}
