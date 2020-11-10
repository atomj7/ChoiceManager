package com.choicemanager.config;

import com.choicemanager.domain.Role;
import com.choicemanager.domain.User;
import com.choicemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**", "/home", "/registration", "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        String userByMailQuery = "SELECT email, password, is_active FROM usr WHERE email = ?;";
        String userByLoginQuery = "SELECT login, password, is_active FROM usr WHERE login=?";
        String roleByLoginQuery = "select u.login, ur.role from usr u inner join user_role ur on u.id = ur.user_id where u.login=?";
        String roleByMailQuery = "select u.email, ur.role from usr u inner join user_role ur on u.id = ur.user_id where u.email=?";

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery(userByMailQuery)
                .authoritiesByUsernameQuery(roleByMailQuery);

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery(userByLoginQuery)
                .authoritiesByUsernameQuery(roleByLoginQuery);

    }

    @Bean
    PrincipalExtractor principalExtractor(UserRepository userRepository) {
        return map -> {
            String id = (String) map.get("sub");
            User user = userRepository.findById(id).orElseGet(() -> {
                User newUser = new User();

                newUser.setId(id);
                newUser.setName((String) map.get("given_name"));
                newUser.setSurname((String) map.get("family_name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setUserPic((String) map.get("picture"));
                newUser.setLocale((String) map.get("locale"));
                newUser.setRole(Collections.singleton(Role.USER));

                return newUser;
            });

            user.setLastVisit(LocalDateTime.now());

            return userRepository.save(user);
        };
    }

}

