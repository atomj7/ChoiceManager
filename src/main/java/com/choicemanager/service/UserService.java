package com.choicemanager.service;

import com.choicemanager.domain.Role;
import com.choicemanager.domain.User;
import com.choicemanager.repository.UserRepository;
import com.choicemanager.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipal;
import java.util.*;

@Service("UserService")
public class UserService implements UserDetailsService {

    @Value("${hostname}")
    private String hostname;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final RoleService roleService;
    private final MailSender mailSender;

    UserService(UserRepository userRepository,
                PasswordEncoder passwordEncoder,
                EntityManager entityManager,
                RoleService roleService, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
        this.roleService = roleService;
        this.mailSender = mailSender;
    }


    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return user;
    }

    public boolean addUser(User userData) {
        if (isUserExist(userData)) {
            return false;
        }

        User newUser = new User(userData);
        newUser.setActive(true);
        newUser.setRoles(roleService.addRole(
                Collections.singleton(new Role(1L, "ROLE_USER"))));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(newUser);

        if (!StringUtils.isEmpty(newUser.getEmail())) {
            String message = String.format(
                    "Hello, %s %s! \n" +
                            "Welcome to Choice Manager.\n " +
                            "Please, visit next link: http://%s:8080/activate/%s to activate your account.\n " +
                            "Have a nice day!",
                    newUser.getName(),
                    newUser.getSurname(),
                    hostname,
                    newUser.getActivationCode()
            );
            mailSender.sendActivationCode(newUser.getEmail(), "Activation code", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        Optional<User> user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.get().setActivated(true);
        user.get().setActivationCode("activated");
        userRepository.save(user.get());

        return true;
    }

    public boolean isActivated(Long id) {
        return userRepository.findById(id).map(User::isActivated).orElse(false);
    }

    public String getActivationCodeById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get().getActivationCode();
        }
        return "User are not present";
    }

    public ResponseEntity<Object> errorValidationProcessing(BindingResult bindingResult, User userData) {
        Map<String, String> errorsMap = ErrorUtils.getErrors(bindingResult);
        if (userData == null) {
            errorsMap.put("data", "data is empty");
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorsMap);
        }
        if (userData.getPassword() != null &&
                !userData.getPassword().equals(userData.getPasswordConfirmation())) {
            errorsMap.put("passwordError", "Password are different!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorsMap);
        }
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorsMap);

        }
        return null;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public boolean isUserExist(User user) {
        return (userRepository.findByEmail(user.getEmail()).isPresent())
                || (userRepository.findByLogin(user.getLogin()).isPresent());
    }

    public Object allUsers() {
        return userRepository.findAll();
    }

    public List<User> userGetList(Long idMin) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
