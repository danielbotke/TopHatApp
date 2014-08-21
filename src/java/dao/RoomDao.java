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

    public Room get(long id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Room.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(Room r) {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            
            for (int i = 0; i < r.getDevices().size(); i++) {
                DeviceDao dao = new DeviceDao();
                r.getDevices().get(i).setRoom(r);
                dao.save(r.getDevices().get(i));
            }
            if (r.getId() == 0) {
                em.persist(r);
            } else {
                em.merge(r);
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
