package de.sg.computerinsel.tools;

import java.util.Date;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.sg.computerinsel.tools.reparatur.model.Filiale;
import de.sg.computerinsel.tools.reparatur.model.IntegerBaseObject;
import de.sg.computerinsel.tools.reparatur.model.Kunde;
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
        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public Map<String, Object> getConnectionProperties() {
        return sessionFactory.getProperties();
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

    public List<? extends IntegerBaseObject> listByConditions(final Class<? extends IntegerBaseObject> clzz,
            final Map<String, ?> conditions) {
        final Session session = sessionFactory.openSession();
        final Criteria criteria = session.createCriteria(clzz);
        addConditionsToCriteria(conditions, criteria);
        @SuppressWarnings("unchecked")
        final List<IntegerBaseObject> list = criteria.list();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Anzahl Einträge: {}", list.size());
            list.forEach(o -> LOGGER.debug(o.toString()));
        }
        session.close();
        return list;
    }

    private void addConditionsToCriteria(final Map<String, ?> conditions, final Criteria criteria) {
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
    }

    public List<IntegerBaseObject> listByDate(final Date startDate, final Date endDate, final Map<String, ?> conditions) {
        final Session session = sessionFactory.openSession();
        final Criteria criteria = session.createCriteria(Reparatur.class);
        addConditionsToCriteria(conditions, criteria);
        final String reparaturField = getFieldNameByAnnotation(Reparatur.class, "abholdatum");
        if (startDate != null) {
            criteria.add(Restrictions.ge(reparaturField, startDate));
        }
        if (endDate != null) {
            criteria.add(Restrictions.le(reparaturField, endDate));
        }
        criteria.addOrder(Order.asc(reparaturField));
        criteria.addOrder(Order.asc(getFieldNameByAnnotation(Reparatur.class, "abholzeit")));
        criteria.addOrder(Order.asc(getFieldNameByAnnotation(Reparatur.class, "nummer")));
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

    public Map<String, ?> createConditions(final IntegerBaseObject obj) {
        final Map<String, Object> conditions = new HashMap<>();
        if (obj instanceof Kunde) {
            final Kunde kunde = (Kunde) obj;
            if (StringUtils.isNotBlank(kunde.getNachname())) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "nachname"), kunde.getNachname());
            }
            if (StringUtils.isNotBlank(kunde.getVorname())) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "vorname"), kunde.getVorname());
            }
            if (StringUtils.isNotBlank(kunde.getStrasse())) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "strasse"), kunde.getStrasse());
            }
            if (StringUtils.isNotBlank(kunde.getPlz())) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "plz"), kunde.getPlz());
            }
            if (StringUtils.isNotBlank(kunde.getOrt())) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "ort"), kunde.getOrt());
            }
            if (StringUtils.isNotBlank(kunde.getTelefon())) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "telefon"), kunde.getTelefon());
            }
            if (StringUtils.isNotBlank(kunde.getEmail())) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "email"), kunde.getEmail());
            }
        } else if (obj instanceof Reparatur) {
            final Reparatur reparatur = (Reparatur) obj;
            if (reparatur.getErledigt() != null) {
                conditions.put(getFieldNameByAnnotation(obj.getClass(), "erledigt"), reparatur.getErledigt());
            }
        }
        return conditions;
    }

    private String getFieldNameByAnnotation(final Class<? extends IntegerBaseObject> clzz, final String field) {
        try {
            return clzz.getDeclaredField(field).getAnnotation(Column.class).name();
        } catch (NoSuchFieldException | SecurityException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public Integer getMaxId(final Class<? extends IntegerBaseObject> clzz) {
        final Session session = sessionFactory.openSession();
        final Integer maxId = (Integer) session.createNativeQuery("call NEXT VALUE FOR R_NUMMER_SEQUENCE").getResultList().get(0);
        session.close();
        return maxId == null ? 0 : maxId;
    }

}