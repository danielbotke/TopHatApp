/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.AirConditioner;
import models.Device;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class AirConditionerDao {

    public AirConditioner get(int id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(AirConditioner.class, id);
        } finally {
            em.close();
        }
    }
    
        public AirConditioner getByDevice(int deviceId) {
        EntityManager em = JpaUtil.get().getEntityManager();
        
        Query q = em.createQuery("select a from AirConditioner a where a.device.id = " + deviceId);
        try {
            if (q.getResultList().size() > 0) {
                return (AirConditioner) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(AirConditioner a) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Boolean result = this.save(a, em);
        em.close();
        return result;
    }

    public boolean save(AirConditioner a, EntityManager em) {
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(a.getId()) == null) {
                em.persist(a);
            } else {
                a.setId((this.get(a.getId())).getId());
                em.merge(a);
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

    public List<Device> list(int roomId) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Device as a where a.room.id = " + roomId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
        public List<Device> listAll() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select a from Device as a");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
