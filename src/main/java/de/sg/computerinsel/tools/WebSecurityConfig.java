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
            .antMatchers("/kassenbuch.html*").hasRole("ZUGRIFF_KASSENBUCH_ERSTELLEN")
            .antMatchers("/kassenbuch-uebersicht.html*", "/kassenbuch*").hasRole("ZUGRIFF_KASSENBUCH_UEBERSICHT")
            .antMatchers("/kassenbuch-drucken.html*").hasRole("KASSENBUCH")
            .antMatchers(HttpMethod.PUT, "/kassenbuch").hasRole("ZUGRIFF_KASSENBUCH_ERSTELLEN")
            .antMatchers(HttpMethod.DELETE, "/kassenbuch").hasRole("KASSENBUCH_VERWALTEN")
            .antMatchers("/kassenstand.html*", "/kassenbuch/kassenstand*").hasRole("ZUGRIFF_KASSENBUCH_KASSENSTAND")
            .antMatchers("/statistiken.html*", "/kassenbuch/statistiken*").hasRole("ZUGRIFF_KASSENBUCH_STATISTIK")
            .antMatchers("/einstellungen.html*", "/einstellungen").hasRole("ZUGRIFF_EINSTELLUNGEN_ALLGEMEIN")
            .antMatchers(HttpMethod.PUT, "/einstellungen/filiale").hasRole("FILIALEN_VERWALTEN")
            .antMatchers(HttpMethod.GET, "/einstellungen/filiale").authenticated()
            .antMatchers("/filiale.html*", "/einstellungen/filiale*").hasRole("ZUGRIFF_EINSTELLUNGEN_FILIALEN")
            .antMatchers("/einstellungen/mitarbeiter/rechte").hasRole("MITARBEITER_RECHTE")
            .antMatchers("/einstellungen/mitarbeiter/reset").hasRole("MITARBEITER_RESET")
            .antMatchers(HttpMethod.PUT, "/einstellungen/mitarbeiter").hasRole("MITARBEITER_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/einstellungen/mitarbeiter").hasRole("MITARBEITER_VERWALTEN")
            .antMatchers("/mitarbeiter.html*", "/einstellungen/mitarbeiter*").hasRole("ZUGRIFF_EINSTELLUNGEN_MITARBEITER")
            .antMatchers(HttpMethod.PUT, "/kunde").hasRole("KUNDEN_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/kunde").hasRole("KUNDEN_VERWALTEN")
            .antMatchers("/kunden.html*").hasRole("ZUGRIFF_KUNDEN")
            .antMatchers(HttpMethod.PUT, "/reparatur/erledigen").hasRole(REPARATUR_VERWALTEN)
            .antMatchers(HttpMethod.PUT, "/reparatur").hasRole(REPARATUR_VERWALTEN)
            .antMatchers(HttpMethod.DELETE, "/reparatur").hasRole(REPARATUR_VERWALTEN)
            .antMatchers("/reparatur-drucken.html*").hasRole("REPARATUR")
            .antMatchers("/reparatur.html*").hasRole("ZUGRIFF_REPARATUR_ERSTELLEN")
            .antMatchers("/reparatur-uebersicht.html?id=*").hasRole("KUNDEN_REPARATUR")
            .antMatchers("/reparatur-uebersicht.html*", "/reparatur*").hasRole("ZUGRIFF_REPARATUR_UEBERSICHT")
            .antMatchers(HttpMethod.PUT, "/rechnung").hasRole("RECHNUNG_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/rechnung").hasRole("RECHNUNG_VERWALTEN")
            .antMatchers("/rechnung-drucken.html*").hasRole("RECHNUNG")
            .antMatchers("/rechnung.html*").hasRole("ZUGRIFF_RECHNUNG_ERSTELLEN")
            .antMatchers("/rechnung-uebersicht.html?id=*").hasRole("KUNDEN_RECHNUNG")
            .antMatchers("/rechnung-uebersicht.html*", "/rechnung*").hasRole("ZUGRIFF_RECHNUNG_UEBERSICHT")
            .antMatchers("/rechnung-export.html*", "/rechnung/export").hasRole("ZUGRIFF_RECHNUNG_EXPORT")
            .antMatchers("/rechnung-storno-drucken.html*").hasRole("RECHNUNG_STORNO")
            .antMatchers(HttpMethod.GET, "/rechnung/stornobeleg").hasRole("RECHNUNG_STORNO")
            .antMatchers("/rechnung-storno-uebersicht.html*").hasRole("ZUGRIFF_RECHNUNG_STORNO_UEBERSICHT")
            .antMatchers(HttpMethod.DELETE, "/rechnung/storno").hasRole("RECHNUNG_STORNO_VERWALTEN")
            .antMatchers(HttpMethod.PUT, "/rechnung/export").hasRole("RECHNUNG_EXPORT")
            .antMatchers("/produkt.html*", "/inventar/produkt*").hasRole("ZUGRIFF_INVENTAR_PRODUKT")
            .antMatchers(HttpMethod.PUT, "/inventar/produkt").hasRole("INVENTAR_PRODUKT_VERWALTEN")
            .antMatchers(HttpMethod.PUT, "/inventar/produkt/mwst").hasRole("INVENTAR_PRODUKT_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/inventar/produkt").hasRole("INVENTAR_PRODUKT_VERWALTEN")
            .antMatchers("/gruppe.html*", "/inventar/gruppe*").hasRole("ZUGRIFF_INVENTAR_GRUPPE")
            .antMatchers(HttpMethod.PUT, "/inventar/gruppe").hasRole("INVENTAR_GRUPPE_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/inventar/gruppe").hasRole("INVENTAR_GRUPPE_VERWALTEN")
            .antMatchers("/kategorie.html*", "/inventar/kategorie*").hasRole("ZUGRIFF_INVENTAR_KATEGORIE")
            .antMatchers(HttpMethod.PUT, "/inventar/kategorie").hasRole("INVENTAR_KATEGORIE_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/inventar/kategorie").hasRole("INVENTAR_KATEGORIE_VERWALTEN")
            .antMatchers("/export.html*", "/inventar/export").hasRole("ZUGRIFF_INVENTAR_EXPORT")
            .antMatchers(HttpMethod.PUT, "/inventar/export").hasRole("INVENTAR_EXPORT")
            .antMatchers("/import.html*", "/inventar/import").hasRole("ZUGRIFF_INVENTAR_IMPORT")
            .antMatchers(HttpMethod.PUT, "/inventar/import").hasRole("INVENTAR_IMPORT")
            .antMatchers("/inventur.html*").hasRole("ZUGRIFF_INVENTAR_INVENTUR")
            .antMatchers(HttpMethod.POST, "/inventar/inventur").hasRole("ZUGRIFF_INVENTAR_INVENTUR")
            .antMatchers("/einkauf.html*", "/einkauf*").hasRole("ZUGRIFF_EINKAUF")
            .antMatchers(HttpMethod.PUT, "/bestellung").hasRole("BESTELLUNG_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/bestellung").hasRole("BESTELLUNG_VERWALTEN")
            .antMatchers("/bestellung-drucken.html*").hasRole("BESTELLUNG")
            .antMatchers("/bestellung.html*").hasRole("ZUGRIFF_BESTELLUNG_ERSTELLEN")
            .antMatchers("/bestellung-uebersicht.html?id=*").hasRole("KUNDEN_BESTELLUNGEN")
            .antMatchers("/bestellung-uebersicht.html*", "/bestellung*").hasRole("ZUGRIFF_BESTELLUNG_UEBERSICHT")
            .antMatchers(HttpMethod.PUT, "/angebot").hasRole("ANGEBOT_VERWALTEN")
            .antMatchers(HttpMethod.DELETE, "/angebot").hasRole("ANGEBOT_VERWALTEN")
            .antMatchers("/angebot-drucken.html*").hasRole("ANGEBOT")
            .antMatchers("/angebot.html*").hasRole("ZUGRIFF_ANGEBOT_ERSTELLEN")
            .antMatchers("/angebot-uebersicht.html?id=*").hasRole("KUNDEN_ANGEBOT")
            .antMatchers("/angebot-uebersicht.html*", "/angebot*").hasRole("ZUGRIFF_ANGEBOT_UEBERSICHT")
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
