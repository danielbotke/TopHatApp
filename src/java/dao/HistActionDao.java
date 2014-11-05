/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.Device;
import models.HistAction;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class HistActionDao {

    public HistAction get(int id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(HistAction.class, id);
        } finally {
            em.close();
        }
    }
    
    public boolean save(HistAction h) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Boolean result = this.save(h, em);
        em.close();
        return result;
    }

    public boolean save(HistAction h, EntityManager em) {
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(h.getId()) == null) {
                em.persist(h);
            } else {
                em.merge(h);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public boolean delete(HistAction h) {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            HistAction hist = em.merge(h);
            em.remove(hist);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<HistAction> list() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from HistAction as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    public List<HistAction> listPeriod(Date dataIni, Date dataEnd, String action, Device dev) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select h from HistAction as h where h.dateTime BETWEEN :dataIni AND :dataEnd and h.action.name = :action and h.action.device.id = :dev");
            q.setParameter("dataIni", dataIni);
            q.setParameter("dataEnd", dataEnd);
            q.setParameter("action", action);
            q.setParameter("dev", dev.getId());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
