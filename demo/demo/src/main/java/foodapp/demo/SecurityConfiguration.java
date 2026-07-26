    package foodapp.demo;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.provisioning.InMemoryUserDetailsManager;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;

    @Configuration
    public class SecurityConfiguration {
        @Autowired
        private JwtFilter jwtFilter;
        @Bean
        public SecurityFilterChain securityfilterchain(HttpSecurity http)
                throws Exception {

            http.cors(Customizer.withDefaults())
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/redis/**").permitAll()
                            .requestMatchers(
                                    "/payment.html",
                                    "/**/*.html",
                                    "/**/*.js",
                                    "/**/*.css"
                            ).permitAll()
                            .requestMatchers("/payment").permitAll()
                            .requestMatchers("/call").permitAll()
                            .requestMatchers("/verify-otp").permitAll()
                            .requestMatchers("/send-otp").permitAll()
                            .requestMatchers("/restaurent/**").permitAll()
                            .requestMatchers("/searchmenu/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"/admin/**").hasRole("ADMIN")
                            .requestMatchers("/cancelorder/**").permitAll()
                            .requestMatchers("/orders/myorders/**").permitAll()
                            .requestMatchers("/register/**").permitAll()
                            .requestMatchers("/addtocart/**").permitAll()
                            .requestMatchers("/viewCart/**").permitAll()
                            .requestMatchers("/removeFromcart/**").permitAll()
                            .requestMatchers("/upload/**").permitAll()
                            .requestMatchers("/cascade/**").permitAll()
                            .requestMatchers("/lazyload/**").permitAll()
                            .requestMatchers("/login").permitAll()
                            .requestMatchers("/addRestaurent").permitAll()
                            .requestMatchers("/test").permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    "/FoodItems/**")
                            .permitAll()

                            .requestMatchers(HttpMethod.POST,
                                    "/FoodItems/**")
                            .hasRole("ADMIN")

                            .requestMatchers(HttpMethod.PUT,
                                    "/FoodItems/**")
                            .hasRole("ADMIN")

                            .requestMatchers(HttpMethod.DELETE,
                                    "/FoodItems/**")
                            .hasRole("ADMIN")

                            .anyRequest().authenticated()
                    )
                    .httpBasic(httpBasic -> httpBasic.disable());
            http.addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );
            return http.build();
        }
        @Bean
        public AuthenticationManager authenticationManager(
                AuthenticationConfiguration config)
                throws Exception {

            return config.getAuthenticationManager();
        }
        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder();
        }
    }
