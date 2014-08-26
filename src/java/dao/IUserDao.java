/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import models.IUser;
import utils.JpaUtil;

/**
 *
 * @author Daniel
 */
public class IUserDao {

    public IUser get(int id) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            return em.find(IUser.class, id);
        } finally {
            em.close();
        }
    }

    public boolean save(IUser u) {
        EntityManager em = JpaUtil.get().getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();

        try {
            if (this.get(u.getId()) == null) {
                em.persist(u);
            } else {
                em.merge(u);
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

    public boolean delete(IUser u) {
        EntityManager em = JpaUtil.get().getEntityManager();
        em.getTransaction().begin();
        try {
            IUser user = em.merge(u);
            em.remove(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<IUser> list() {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select u from IUser as u");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public IUser getFacebookId(String facebookId) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select u from IUser as u where u.facebookId = " + facebookId);
            List result = q.getResultList();
            if(result != null){
                return (IUser)result.get(0);
            }else{
                return null;
            }
        } finally {
            em.close();
        }
    }
}
