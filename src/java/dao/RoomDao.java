/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.Room;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class RoomDao {

    public Room get(Room r) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select r from Room r where r.home.id = " + r.getHome().getId() + " and r.name = '" + r.getName()+ "'");
        try {
            if (q.getResultList().size() > 0) {
                return (Room)q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Room r) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Boolean result = this.save(r, em);
        em.close();
        return result;
    }

    public boolean save(Room r, EntityManager em) {
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(r) == null) {
                em.persist(r);
            } else {
                r.setId((this.get(r)).getId());
                em.merge(r);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public boolean delete(Room r) {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Room room = em.merge(r);
            em.remove(room);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Room> list() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Room as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
