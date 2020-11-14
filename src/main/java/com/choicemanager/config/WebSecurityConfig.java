package com.choicemanager.config;

import com.choicemanager.service.AuthenticationService;
import com.choicemanager.service.RoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
//@EnableOAuth2Sso
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final RoleService roleService;
    private final AuthenticationService authenticationService;

    WebSecurityConfig(DataSource dataSource,
                      RoleService roleService,
                      @Lazy BCryptPasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.dataSource = dataSource;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    CorsFilter corsFilter() {
        CorsFilter filter = new CorsFilter();
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .addFilterBefore(corsFilter(), SessionManagementFilter.class) //adds your custom CorsFilter
                    .exceptionHandling()
                .and()
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
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService);
    }
    /*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        String userByMailQuery = "SELECT email, password, active " +
                "FROM usr WHERE email = ?";
        String userByLoginQuery = "SELECT login, password, active " +
                "FROM usr WHERE login = ?";
        String roleByLoginQuery = "select u.login, r.name " +
                "from roles r, usr u, usr_roles " +
                "where u.login = ? " +
                "and r.id = usr_roles.users_id";
        String roleByEmailQuery = "select u.email, r.name " +
                "from roles r, usr u, usr_roles " +
                "where u.email = ? " +
                "and r.id = usr_roles.users_id";
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery(userByMailQuery)
                .authoritiesByUsernameQuery(roleByLoginQuery);

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery(userByLoginQuery)
                .authoritiesByUsernameQuery(roleByEmailQuery);

    }*/

   /* PrincipalExtractor principalExtractor(UserRepository userRepository) {
        return map -> {
            User newUser = new User();
            String email = (String) map.get("email");
            try {
                newUser = userRepository.findByEmail(email);
            } catch (NullPointerException e) {
                newUser.setGoogleAuthId((String) map.get("sdf"));
                newUser.setName((String) map.get("given_name"));
                newUser.setSurname((String) map.get("family_name"));
                newUser.setEmail((String) map.get("email"));
                newUser.setUserPic((String) map.get("picture"));
                newUser.setLocale((String) map.get("locale"));
                newUser.setRoles(roleService.addRole(
                        Collections.singleton(new Role(1L, "ROLE_USER"))));
            }
           newUser.setLastVisit(LocalDateTime.now());

            return userRepository.save(newUser);
        };
    }*/

}

