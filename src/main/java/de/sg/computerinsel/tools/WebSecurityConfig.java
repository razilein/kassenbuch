package de.sg.computerinsel.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REPARATUR_VERWALTEN = "REPARATUR_VERWALTEN";
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        /* @formatter:off */
        http
        .sessionManagement()
          .and()
        .authorizeRequests()
            .antMatchers("/themes/**", "/scripts/**", "/login**").permitAll()
            .antMatchers("/kassenbuch.html*", "/kassenbuch/erstellen*", "kassenbuch/download*", "/kassenbuch/eintragungen/erstellen*").hasRole("ZUGRIFF_KASSENBUCH_ERSTELLEN")
            .antMatchers("/kassenstand.html*", "/kassenbuch/kassenstand*").hasRole("ZUGRIFF_KASSENBUCH_KASSENSTAND")
            .antMatchers("/statistiken.html*", "/kassenbuch/statistiken*").hasRole("ZUGRIFF_KASSENBUCH_STATISTIK")
            .antMatchers("/einstellungen.html*", "/einstellungen").hasRole("ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN")
            .antMatchers(HttpMethod.PUT, "/einstellungen/filiale").hasRole("FILIALEN_VERWALTEN")
            .antMatchers("/filiale.html*", "/einstellungen/filiale*").hasRole("ZUGRIFF_EINSTELLUNGEN_FILIALEN")
            .antMatchers("/einstellungen/mitarbeiter/rechte").hasRole("MITARBEITER_RECHTE")
            .antMatchers("/einstellungen/mitarbeiter/reset").hasRole("MITARBEITER_RESET")
            .antMatchers(HttpMethod.PUT, "/einstellungen/mitarbeiter").hasRole("MITARBEITER_VERWALTEN")
            .antMatchers("/mitarbeiter.html*", "/einstellungen/mitarbeiter*").hasRole("ZUGRIFF_EINSTELLUNGEN_MITARBEITER")
            .antMatchers(HttpMethod.PUT, "/reparatur/kunde").hasRole("KUNDEN_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/reparatur/kunde").hasRole("KUNDEN_VERWALTEN")
            .antMatchers("/kunden.html*").hasRole("ZUGRIFF_KUNDEN")
            .antMatchers(HttpMethod.PUT, "/reparatur/erledigen").hasRole(REPARATUR_VERWALTEN)
            .antMatchers(HttpMethod.PUT, "/reparatur").hasRole(REPARATUR_VERWALTEN)
            .antMatchers(HttpMethod.DELETE, "/reparatur").hasRole(REPARATUR_VERWALTEN)
            .antMatchers("/reparatur-drucken.html*").hasRole("REPARATUR")
            .antMatchers("/reparatur.html*").hasRole("ZUGRIFF_REPARATUR_ERSTELLEN")
            .antMatchers("/reparatur-uebersicht.html?id=*").hasRole("KUNDEN_REPARATUR")
            .antMatchers("/reparatur-uebersicht.html*", "/reparatur*").hasRole("ZUGRIFF_REPARATUR_UEBERSICHT")
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login.html")
            .and()
         .csrf()
            .disable()
        .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login.html")
            .deleteCookies("JSESSIONID")
            .permitAll()
            .and()
        .exceptionHandling()
            .accessDeniedPage("/no-permission.html");
        /* @formatter:on */
    }

}
