package de.sg.computerinsel.tools;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
/* @formatter:off */
@EnableJpaRepositories(
        basePackages = {
          "de.sg.computerinsel.tools.angebot",
          "de.sg.computerinsel.tools.bestellung",
          "de.sg.computerinsel.tools.einkauf",
          "de.sg.computerinsel.tools.kassenbuch",
          "de.sg.computerinsel.tools.kunde",
          "de.sg.computerinsel.tools.model",
          "de.sg.computerinsel.tools.rechnung",
          "de.sg.computerinsel.tools.reparatur"
        },
        entityManagerFactoryRef = "kasseEntityManager")
/* @formatter:on */
public class KasseConfig {

    @Primary
    @Bean(name = "kasseDataSourceProperties")
    @ConfigurationProperties("kasse.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "kasseDataSource")
    @ConfigurationProperties("kasse.datasource.configuration")
    public DataSource dataSource(@Qualifier("kasseDataSourceProperties") final DataSourceProperties kasseDataSourceProperties) {
        return kasseDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "kasseEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder,
            @Qualifier("kasseDataSource") final DataSource kasseDataSource) {
        return builder.dataSource(kasseDataSource)
                .packages("de.sg.computerinsel.tools.angebot", "de.sg.computerinsel.tools.bestellung", "de.sg.computerinsel.tools.einkauf",
                        "de.sg.computerinsel.tools.kassenbuch", "de.sg.computerinsel.tools.kunde", "de.sg.computerinsel.tools.model",
                        "de.sg.computerinsel.tools.rechnung", "de.sg.computerinsel.tools.reparatur")
                .persistenceUnit("kasse").build();
    }

    @Primary
    @Bean(name = "kasseTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("kasseEntityManagerFactory") final EntityManagerFactory kasseEntityManagerFactory) {
        return new JpaTransactionManager(kasseEntityManagerFactory);
    }

}