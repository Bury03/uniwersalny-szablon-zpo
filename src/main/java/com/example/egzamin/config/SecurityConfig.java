package com.example.egzamin.config;

import com.example.egzamin.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
    ============================================================
    SECURITY CONFIG
    ============================================================

    Ten plik odpowiada za bezpieczeństwo aplikacji:

    - logowanie,
    - wylogowanie,
    - dostęp do stron publicznych,
    - dostęp do panelu użytkownika,
    - dostęp do panelu administratora,
    - szyfrowanie haseł.

    To jest plik raczej STAŁY.
    Na egzaminie najczęściej tylko kopiujesz i ewentualnie zmieniasz ścieżki.
*/

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                    Podpinamy nasz własny serwis użytkowników.

                    CustomUserDetailsService pobiera użytkownika z bazy danych
                    po username i przekazuje go do Spring Security.
                */
                .userDetailsService(customUserDetailsService)

                /*
                    Ustawiamy dostęp do stron.
                */
                .authorizeHttpRequests(auth -> auth

                        /*
                            Strony publiczne.
                            Tutaj może wejść każdy, nawet niezalogowany.
                        */
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                            Panel administratora i admin REST API.

                            Dostęp tylko dla roli ADMIN.
                        */
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")

                        /*
                            Panel użytkownika.

                            Dostęp dla USER i ADMIN.
                            Dzięki temu admin może też wejść w panel użytkownika.
                        */
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        /*
                            Pozostałe endpointy REST API wymagają zalogowania.
                        */
                        .requestMatchers("/api/**").authenticated()

                        /*
                            Każda inna strona też wymaga zalogowania.
                        */
                        .anyRequest().authenticated()
                )

                /*
                    Konfiguracja formularza logowania.
                */
                .formLogin(login -> login

                        /*
                            Nasza własna strona logowania:
                            templates/login.html
                        */
                        .loginPage("/login")

                        /*
                            Po poprawnym logowaniu przekierowujemy użytkownika
                            zależnie od roli.

                            ADMIN idzie do:
                            /admin/panel

                            USER idzie do:
                            /user/panel
                        */
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(authority ->
                                            authority.getAuthority().equals("ROLE_ADMIN")
                                    );

                            if (isAdmin) {
                                response.sendRedirect("/admin/panel");
                            } else {
                                response.sendRedirect("/user/panel");
                            }
                        })

                        .permitAll()
                )

                /*
                    Konfiguracja wylogowania.
                */
                .logout(logout -> logout

                        /*
                            Po wylogowaniu użytkownik wraca na login
                            z parametrem logout.

                            Dzięki temu w login.html pokazujemy komunikat:
                            "Zostałeś poprawnie wylogowany."
                        */
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                /*
                    CSRF zostawiamy włączone dla formularzy HTML.

                    Wyłączamy tylko dla:
                    - H2 console,
                    - REST API.

                    Dzięki temu formularze z POST muszą mieć token CSRF.
                */
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/api/**")
                )

                /*
                    To jest potrzebne, żeby działała konsola H2 w przeglądarce.
                */
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }

    /*
        Bean do szyfrowania haseł.

        Używany w:
        - UserService,
        - DataInitializer.

        Dzięki temu hasła nie są zapisane w bazie jako zwykły tekst.
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}