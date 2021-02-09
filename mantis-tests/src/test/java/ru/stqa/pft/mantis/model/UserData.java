package ru.stqa.pft.mantis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "mantis_user_table")
public class UserData {

  @Id
  @Column(name = "id")
  private int id;

  @Column(name = "username")
  private String username;

  @Column(name = "email")
  private String email;

  @Column(name = "access_level")
  private short access;

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public short getAccess() {
    return access;
  }

  public UserData setId(int id) {
    this.id = id;
    return this;
  }

  public UserData withUsername(String username) {
    this.username = username;
    return this;
  }

  public UserData setEmail(String email) {
    this.email = email;
    return this;
  }

  public UserData setAccess(short access) {
    this.access = access;
    return this;
  }

  @Override
  public String toString() {
    return "UserData{" +
            "id=" + id +
            ", username='" + username + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;
    UserData userData = (UserData) o;
    return Objects.equals(username, userData.username) && Objects.equals(email, userData.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email);
  }
}
