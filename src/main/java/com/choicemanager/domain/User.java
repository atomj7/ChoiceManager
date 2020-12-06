package com.choicemanager.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usr")
@Data
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
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

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime lastVisit;

    private boolean active;

    private String locale;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Goal> goals;

    private String activationCode;

    private boolean emailConfirmed;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    private boolean isTested;

    public User() {
    }

    public User(String username, String email, String password, String name, String surname) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(User user) {
        this.id = user.getId();
        this.username= user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.passwordConfirmation = user.getPasswordConfirmation();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.imageUrl = user.getImageUrl();
        this.gender = user.getGender();
        this.lastVisit = user.getLastVisit();
        this.active = user.active;
        this.locale = user.getLocale();
        this.roles = user.getRoles();
        this.goals = user.getGoals();
        this.emailConfirmed = user.isEmailConfirmed();
        this.activationCode = user.activationCode;
        this.provider = user.provider;
        this.providerId = user.providerId;
        this.isTested = user.isTested;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return this.username;
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

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
