package de.sg.computerinsel.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            .antMatchers("/kassenbuch.html", "/kassenbuch/erstellen*", "kassenbuch/download*", "/kassenbuch/eintragungen/erstellen*" ).hasAnyRole("ZUGRIFF_KASSENBUCH_ERSTELLEN")
            .antMatchers("/kassenstand.html", "/kassenbuch/kassenstand*").hasAnyRole("ZUGRIFF_KASSENBUCH_KASSENSTAND")
            .antMatchers("/statistiken.html", "/kassenbuch/statistiken*").hasAnyRole("ZUGRIFF_KASSENBUCH_STATISTIK")
            .antMatchers("/einstellungen.html", "/einstellungen").hasAnyRole("ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN")
            .antMatchers("/filiale.html", "/einstellungen/filiale*").hasAnyRole("ZUGRIFF_EINSTELLUNGEN_FILIALEN")
            .antMatchers("/mitarbeiter.html", "/einstellungen/mitarbeiter*").hasAnyRole("ZUGRIFF_EINSTELLUNGEN_MITARBEITER")
            .antMatchers("/kunden.html").hasAnyRole("ZUGRIFF_REPARATUR_KUNDEN")
            .antMatchers("/reparatur.html").hasAnyRole("ZUGRIFF_REPARATUR_ERSTELLEN")
            .antMatchers("/reparatur-uebersicht.html", "/reparatur*").hasAnyRole("ZUGRIFF_REPARATUR_UEBERSICHT")
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