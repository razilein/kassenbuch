package de.sg.computerinsel.tools;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
import de.sg.computerinsel.tools.reparatur.model.KundeReparatur;
import de.sg.computerinsel.tools.reparatur.model.Mitarbeiter;
import de.sg.computerinsel.tools.reparatur.model.Reparatur;

public class HibernateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateService.class);

    private final SessionFactory sessionFactory;

    public HibernateService() {
        final Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Kunde.class);
        configuration.addAnnotatedClass(Filiale.class);
        configuration.addAnnotatedClass(Mitarbeiter.class);
        configuration.addAnnotatedClass(Reparatur.class);
        configuration.addAnnotatedClass(KundeReparatur.class);
        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public IntegerBaseObject save(final IntegerBaseObject o) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            if (o.getId() == null) {
                session.save(o);
                LOGGER.debug("Erfolgreich erstellt: {}", o.toString());
            } else {
                session.update(o);
                LOGGER.debug("Erfolgreich bearbeitet: {}", o.toString());
            }
            transaction.commit();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            transaction.rollback();
        } finally {
            session.close();
        }
        return o;
    }

    public IntegerBaseObject get(final Class<? extends IntegerBaseObject> clzz, final Integer id) {
        IntegerBaseObject o;
        try (final Session session = sessionFactory.openSession();) {
            o = session.get(clzz, id);
            LOGGER.debug("{} mit id '{}' gefunden: {}", clzz.getName(), id == null ? null : String.valueOf(id),
                    o == null ? null : o.toString());
        }
        return o;
    }

    public List<IntegerBaseObject> list(final Class<? extends IntegerBaseObject> clzz) {
        final Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        final List<IntegerBaseObject> list = session.createQuery("FROM " + clzz.getName()).getResultList();
        session.close();
        LOGGER.debug("Anzahl Einträge: {}", list.size());
        list.forEach(o -> LOGGER.debug(o.toString()));
        return list;
    }

    public void delete(final IntegerBaseObject o) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            session.remove(o);
            transaction.commit();
            LOGGER.debug("Erfolgreich gelöscht: {}", o.toString());
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            transaction.rollback();
        } finally {
            session.close();
        }
    }

}