package com.choicemanager.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="usr")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="login", unique = true)
    @NotNull
    @NotBlank(message = "Login can not be empty")
    private String login;
    @Column(name="email", unique = true)
    @NotNull
    @NotBlank(message = "Email can not be empty")
    @Email(message = "Please provide a valid email id")
    private String email;
    @Column(name="password")
    @NotNull
    @NotBlank(message = "Password can not be empty")
    //@Size(min = 7, max = 16, message = "Password must be at least 8 to 16 characters long")
    private String password;
    @Transient
    @NotBlank(message = "Password confirmation cannot be empty")
    private String passwordConfirmation;
    @Column(name="name")
    @NotNull
    @NotBlank(message = "Name can not be empty")
    private String name;
    @Column(name="surname")
    @NotNull
    @NotBlank(message = "Surname can not be empty")
    private String surname;
    private boolean isActive;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    public User() {}

    public User(String login, String email, String password, String name, String surname, String phoneNumber) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.passwordConfirmation = user.getPasswordConfirmation();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = user.getRole();
        this.isActive = user.isActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getSurname(), user.getSurname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getEmail(), getPassword(), getName(), getSurname());
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
