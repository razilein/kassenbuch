package de.sg.computerinsel.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Sita Geßner
 */
@SpringBootApplication
public class Start extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(Start.class, args);
    }

}
