package com.choicemanager.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name="usr")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "email", unique = true)
    @NotBlank(message = "Email can not be empty")
    @Email(message = "Please provide a valid email id")
    private String email;

    private String password;

    @Transient
    private String passwordConfirmation;

    @NotBlank(message = "Name can not be empty")
    private String name;

    @NotBlank(message = "Surname can not be empty")
    private String surname;

    private String userPic;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime lastVisit;

    private boolean active;

    private String locale;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    public User() {
    }

    public User(String login, String email, String password, String name, String surname) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.passwordConfirmation = user.getPasswordConfirmation();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.userPic = user.getUserPic();
        this.gender = user.getGender();
        this.lastVisit = user.getLastVisit();
        this.active = user.active;
        this.locale = user.getLocale();
        this.role = user.getRole();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;

        return getId().equals(user.getId()) &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(passwordConfirmation, user.passwordConfirmation) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(userPic, user.userPic) &&
                gender == user.gender &&
                Objects.equals(lastVisit, user.lastVisit) &&
                Objects.equals(locale, user.locale) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email, getPassword(), passwordConfirmation, name, surname, userPic, gender, lastVisit, active, locale, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole();
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", userPic='" + userPic + '\'' +
                ", gender=" + gender +
                ", lastVisit=" + lastVisit +
                ", isActive=" + active +
                ", locale='" + locale + '\'' +
                ", role=" + role +
                '}';
    }
}
