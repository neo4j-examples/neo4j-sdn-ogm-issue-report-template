package org.neo4j.ogm.test.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "User")
public class UserImpl extends Entity implements User {

  private String firstName;

  private String lastName;

  private String email;

  public UserImpl() {
    // required by
  }

  public UserImpl(String email, String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getEmail() {
    return email;
  }
}
