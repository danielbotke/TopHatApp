/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.Home;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class HomeDao {

    public Home get(int id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Home.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(Home h) {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(h.getId()) == null) {
                em.persist(h);
            } else {
                em.merge(h);
            }
            trans.commit();
            for (int i = 0; i < h.getRooms().size(); i++) {
                RoomDao dao = new RoomDao();
                h.getRooms().get(i).setHome(h);
                dao.save(h.getRooms().get(i), em);
            }
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(Home h) {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Home home = em.merge(h);
            em.remove(home);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Home> list() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Home as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}