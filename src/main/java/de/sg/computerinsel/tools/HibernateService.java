package de.sg.computerinsel.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Anzahl Einträge: {}", list.size());
            list.forEach(o -> LOGGER.debug(o.toString()));
        }
        return list;
    }

    public List<? extends IntegerBaseObject> listByConditions(final Class<? extends IntegerBaseObject> clzz, final Map<String, ?> conditions) {
        final Session session = sessionFactory.openSession();
        final Criteria criteria = session.createCriteria(clzz);
        for (final Entry<String, ?> entrySet : conditions.entrySet()) {
            if (entrySet.getValue() instanceof String) {
                final String value = StringUtils.replace((String) entrySet.getValue(), "*", "%");
                if (StringUtils.containsAny(value, "%", "_")) {
                    criteria.add(Restrictions.like(entrySet.getKey(), value));
                } else if (StringUtils.isNotBlank(value)) {
                    criteria.add(Restrictions.eq(entrySet.getKey(), value));
                }
            } else {
                criteria.add(Restrictions.eq(entrySet.getKey(), entrySet.getValue()));
            }
        }
        @SuppressWarnings("unchecked")
        final List<IntegerBaseObject> list = criteria.list();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Anzahl Einträge: {}", list.size());
            list.forEach(o -> LOGGER.debug(o.toString()));
        }
        session.close();
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

    public Map<String, String> createConditions(final IntegerBaseObject obj) {
        final Map<String, String> conditions = new HashMap<>();
        if (obj instanceof Kunde) {
            final Kunde kunde = (Kunde) obj;
            try {
                if (StringUtils.isNotBlank(kunde.getNachname())) {
                    conditions.put(Kunde.class.getDeclaredField("nachname").getAnnotation(Column.class).name(), kunde.getNachname());
                }
                if (StringUtils.isNotBlank(kunde.getVorname())) {
                    conditions.put(Kunde.class.getDeclaredField("vorname").getAnnotation(Column.class).name(), kunde.getVorname());
                }
                if (StringUtils.isNotBlank(kunde.getStrasse())) {
                    conditions.put(Kunde.class.getDeclaredField("strasse").getAnnotation(Column.class).name(), kunde.getStrasse());
                }
                if (StringUtils.isNotBlank(kunde.getPlz())) {
                    conditions.put(Kunde.class.getDeclaredField("plz").getAnnotation(Column.class).name(), kunde.getPlz());
                }
                if (StringUtils.isNotBlank(kunde.getOrt())) {
                    conditions.put(Kunde.class.getDeclaredField("ort").getAnnotation(Column.class).name(), kunde.getOrt());
                }
                if (StringUtils.isNotBlank(kunde.getTelefon())) {
                    conditions.put(Kunde.class.getDeclaredField("telefon").getAnnotation(Column.class).name(), kunde.getTelefon());
                }
                if (StringUtils.isNotBlank(kunde.getEmail())) {
                    conditions.put(Kunde.class.getDeclaredField("email").getAnnotation(Column.class).name(), kunde.getEmail());
                }
            } catch (NoSuchFieldException | SecurityException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return conditions;
    }

}