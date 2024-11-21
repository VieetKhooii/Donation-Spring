package com.gabriel.donation.security;

import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.payload.CookieName;
import com.gabriel.donation.service.RoleService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OAuth2AuthorizedClientService clientService;
    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
//                                .anyRequest().permitAll())
                            .requestMatchers("api/auth/**", "login", "error/**", "/").permitAll()
                            .requestMatchers("api/auth/**", "register").permitAll()
                            .requestMatchers("/forgotPassword.html").permitAll()
                            .requestMatchers("/enterOtp").permitAll()
                            .requestMatchers("/resetPassword").permitAll()
                            .requestMatchers("/css/**", "/images/**", "/js/**", "/forgotPassword/**").permitAll()
                            .requestMatchers("api/donation_post/get", "api/donation_post/getDonationPostByID").permitAll()
                            .anyRequest().authenticated())
                .httpBasic(withDefaults())

                .oauth2Login(
//                        Customizer.withDefaults()
                        oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .successHandler((request, response, authentication) -> {
                                    // Extract Google user details
                                    OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
                                    Map<String, Object> attributes = oAuth2Token.getPrincipal().getAttributes();
                                    // Example: Extract email (adjust based on your OAuth provider)
                                    String email = (String) attributes.get("email");
                                    String name = (String) attributes.get("name");

                                    // Find or register user in your database
                                    UserDTO userDTO = userService.findByEmail(email);
                                    if (userDTO == null) {
                                        // Optionally register a new user if they don't exist
//                                        userDTO = userService.registerNewGoogleUser(email, name);
                                        System.out.println("User not found");
                                    }
                                    else {
                                        // Generate JWT token
                                        String token = jwtGenerator.generateToken(email);

                                        // Set session attributes
                                        request.getSession().setAttribute("userId", userDTO.getUserId());
                                        request.getSession().setAttribute("username", userDTO.getName());

                                        // Create cookies
                                        cookieUtil.createNewCookie(response, token, CookieName.jwt);
                                        cookieUtil.createNewCookie(response, userDTO);

                                        // Redirect based on user role
                                        String roleName = roleService.findRoleNameById(userDTO.getRoleId());
                                        if ("USER".equalsIgnoreCase(roleName)) {
                                            response.sendRedirect("/api/donation_post/get"); // Adjust to your user dashboard URL
                                        } else {
                                            response.sendRedirect("/api/admin"); // Adjust to your admin dashboard URL
                                        }
                                    }
                                }
                )
    )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/error/401")))

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
}
