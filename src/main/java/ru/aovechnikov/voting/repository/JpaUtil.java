package ru.aovechnikov.voting.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Utility class for working with {@link EntityManager}.
 *
 * @author - A.Ovechnikov
 * @date - 13.01.2018
 */

public class JpaUtil {

    @PersistenceContext
    private EntityManager em;

    /**
     * Evict all data from the cache.
     */
    public void clear2ndLevelHibernateCache() {
        Session s = (Session) em.getDelegate();
        SessionFactory sf = s.getSessionFactory();
        sf.getCache().evictAllRegions();
    }
}
