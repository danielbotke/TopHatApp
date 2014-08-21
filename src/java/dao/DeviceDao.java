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

    public Device get(int id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(Device.class, id);
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
            if (this.get(d.getId()) == null) {
                em.persist(d);
            } else {
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
