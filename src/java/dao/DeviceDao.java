/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.Device;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class DeviceDao {

    public Device get(int roomId, Device d) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select d from Device d where d.room.id =" + roomId + "and d.name = '" + d.getName() + "'");
        try {
            if (q.getResultList().size() > 0) {
                return (Device) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(Device d) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Boolean result = this.save(d, em);
        em.close();
        return result;
    }

    public boolean save(Device d, EntityManager em) {
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(d.getRoom().getId(), d) == null) {
                em.persist(d);
            } else {
                d.setId((this.get(d.getRoom().getId(), d)).getId());
                em.merge(d);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public boolean delete(Device d) {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            Device device = em.merge(d);
            em.remove(device);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Device> list() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select c from Device as c");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
