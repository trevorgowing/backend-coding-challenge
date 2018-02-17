package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.persistence.AbstractAuditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractAuditable<User, Long> {

  private static final long serialVersionUID = 2138197451190291396L;

  @Basic(optional = false)
  @Column(nullable = false, unique = true)
  private String email;

  @Basic(optional = false)
  @Column(nullable = false)
  private String password;

  private String firstName;

  private String lastName;

  private User(Long id) {
    super(id);
  }

  private User(Long id, String email, String password, String firstName, String lastName) {
    super(id);
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  static User unidentifiedUser(String email, String password, String firstName, String lastName) {
    return new User(email, password, firstName, lastName);
  }

  public static User identifiedUser(Long id) {
    return new User(id);
  }

  static User identifiedUser(Long id, String email, String password, String firstName, String lastName) {
    return new User(id, email, password, firstName, lastName);
  }

  String getName() {
    return firstName + " " + lastName;
  }
}
