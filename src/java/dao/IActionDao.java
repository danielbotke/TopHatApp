/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.IAction;
import models.ToDoAction;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class IActionDao {

    public IAction get(int id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(IAction.class, id);
        } finally {
            em.close();
        }
    }
    
    public IAction get(IAction a) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select a from IAction a where a.name = '" + a.getName() + "' and a.device.id = " + a.getDevice().getId());
        try {
            if (q.getResultList().size() > 0) {
                return (IAction) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public IAction get(ToDoAction t) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select a from IAction a where a.id = (select t.action.id from ToDoAction t where t.id = " + t.getId() + ")");
        try {
            if (q.getResultList().size() > 0) {
                return (IAction) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(IAction a) {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(a.getId()) == null) {
                em.persist(a);
            } else {
                em.merge(a);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(IAction a) {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            IAction action = em.merge(a);
            em.remove(action);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<IAction> list() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from IAction as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
