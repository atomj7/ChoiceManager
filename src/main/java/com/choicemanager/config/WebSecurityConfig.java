package com.choicemanager.config;

import com.choicemanager.security.*;
import com.choicemanager.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.choicemanager.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final RoleService roleService;
    private final AuthenticationService authenticationService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final TokenProvider tokenProvider;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };


    WebSecurityConfig(CustomUserDetailsService customUserDetailsService, CustomOAuth2UserService customOAuth2UserService, DataSource dataSource,
                      RoleService roleService,
                      @Lazy BCryptPasswordEncoder passwordEncoder,
                      AuthenticationService authenticationService,
                      OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                      OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                      HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                      TokenProvider tokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.dataSource = dataSource;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.tokenProvider = tokenProvider;
    }
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
                .authorizeRequests()
                .antMatchers("/**",
                        "/error",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/auth/**", "/oauth2/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
            .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
            .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

      /*  @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        String userByMailQuery = "SELECT email, password, active " +
                "FROM usr WHERE email = ?";
        String userByLoginQuery = "SELECT login, password, active " +
                "FROM usr WHERE login = ?";
        String roleByLoginQuery = "SELECT u.login, ur.name " +
         "FROM usr u INNER JOIN usr_roles ur "+
          "ON u.id = ur.users_id"+
          "WHERE u.login=?";
        String roleByEmailQuery = "SELECT u.email, ur.name " +
         "FROM usr u INNER JOIN usr_roles ur " +
          "ON u.id = ur.users_id " +
         "WHERE u.email?";
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

    }
*/
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

