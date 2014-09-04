/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.ToDoAction;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class ToDoActionDao {

    public ToDoAction get(int id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(ToDoAction.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(ToDoAction t) {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(t.getId()) == null) {
                em.persist(t);
            } else {
                em.merge(t);
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

    public boolean delete(ToDoAction t) {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            ToDoAction toDo = em.merge(t);
            em.remove(toDo);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<ToDoAction> list() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from ToDoAction as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
