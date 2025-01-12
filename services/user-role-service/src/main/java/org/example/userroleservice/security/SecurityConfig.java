package org.example.userroleservice.security;

import org.example.userroleservice.dto.UserDTO;
import org.example.userroleservice.payload.CookieName;
import org.example.userroleservice.service.UserService;
import org.example.userroleservice.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private UserService userService;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private ClientRegistrationRepository  clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .anyRequest().permitAll())
//                            .requestMatchers("api/auth/**", "login", "error/**", "/").permitAll()
//                            .requestMatchers("api/auth/**", "register").permitAll()
//                            .requestMatchers("api/admin/**").hasRole("ADMIN")
//                            .requestMatchers("/forgotPassword.html").permitAll()
//                            .requestMatchers("/enterOtp").permitAll()
//                            .requestMatchers("/resetPassword").permitAll()
//                            .requestMatchers("/css/**", "/images/**", "/js/**", "/forgotPassword/**").permitAll()
//                            .requestMatchers("api/donation_post/get", "api/donation_post/getDonationPostByID", "api/donation_post/getSorted", "api/donation_post/search").permitAll()
//                            .requestMatchers("/api/user_donated/getByDonationPostId").permitAll()
//                            .anyRequest().authenticated())
//                .httpBasic(withDefaults())

                .oauth2Login(
//                        Customizer.withDefaults()
                        oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .authorizationEndpoint(authEndpoint -> authEndpoint
                                        .authorizationRequestResolver(customAuthorizationRequestResolver(clientRegistrationRepository))
                                )
                                .successHandler((request, response, authentication) -> {

                                    OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
                                    Map<String, Object> attributes = oAuth2Token.getPrincipal().getAttributes();

                                    String email = (String) attributes.get("email");
                                    String name = (String) attributes.get("name");

                                    UserDTO userDTO = userService.findByEmail(email);
                                    if (userDTO == null) {
                                        UserDTO newUser = new UserDTO();
                                        newUser.setEmail(email);
                                        newUser.setName(name);
                                        newUser.setPassword("");
                                        newUser.setPhone("phone");
                                        newUser.setBalance(0);
                                        newUser.setRoleId(3);
                                        userService.addUser(newUser);
                                        userDTO = userService.findByEmail(email);
                                    }
                                    String token = jwtGenerator.generateToken(email);

                                    // Create cookies
                                    cookieUtil.createNewCookie(response, token, CookieName.jwt);
                                    cookieUtil.createNewCookie(response, userDTO);

                                    response.sendRedirect("/api/donation_post/get");


                                }
                        )
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/error/401")))
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler((request, response, authException) ->
                                response.sendRedirect("/error/403")))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration(); // This is an example; adjust based on your setup.
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
        return new CustomAuthorizationRequestResolver(defaultResolver);
    }
}
