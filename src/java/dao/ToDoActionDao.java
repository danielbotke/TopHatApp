/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.text.SimpleDateFormat;
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

    public ToDoAction get(ToDoAction todo) {
        EntityManager em = JpaUtil.get().getEntityManager();
        Query q = em.createQuery("select t from ToDoAction t where t.action.id = " + todo.getAction().getId() + " and  t.home.id = '" + todo.getHome().getId() + "' and t.dateTime  = :dataToDo");
        q.setParameter("dataToDo", todo.getDateTime());
        try {
            if (q.getResultList().size() > 0) {
                return (ToDoAction) q.getResultList().get(0);
            } else {
                return null;
            }
        } finally {
            em.close();
        }
    }

    public boolean save(ToDoAction t) {
        String actionDescript = "";
        String deviceDescript = "";
        int temperatura = 0;
        if (t.getAction().getDevice().getType() == 'l') {
            deviceDescript = "luz";
        } else if (t.getAction().getDevice().getType() == 'a') {
            deviceDescript = "ar Condicionado";
        }
        switch (t.getAction().getName()) {
            case "l":
                actionDescript = "Ligar a ";
                break;
            case "d":
                actionDescript = "Desligar a ";
                break;
            case "turnOn":
                actionDescript = "Ligar o ";
                break;
            case "turnOff":
                actionDescript = "Desligar o ";
                break;
            case "swing":
                actionDescript = "Ativar o swing do ";
                break;
            default:
                if (t.getAction().getName().contains("auto")) {
                    actionDescript = "Tempratura do ";
                    temperatura = Integer.parseInt(t.getAction().getName().substring(3));
                }
                break;
        }
        String dia = "";
        if(t.getDateTime().getDay() == 1){
            dia = "fins de semana";
        }else{
            dia = "dias da semana";
    }
        if(temperatura > 0){
            t.setDescription(actionDescript + deviceDescript + " do(a) " + t.getAction().getDevice().getRoom().getName() + " em " + temperatura +" graus, sempre às " + new SimpleDateFormat("HH:mm").format(t.getDateTime()) +" nos " + dia + ".");
        }else{
            t.setDescription(actionDescript + deviceDescript + " do(a) " + t.getAction().getDevice().getRoom().getName() + " sempre às " + new SimpleDateFormat("HH:mm").format(t.getDateTime()) +" nos " + dia + ".");
        }
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

    public List<ToDoAction> list(String homeId) {
        EntityManager em = JpaUtil.get().getEntityManager();
        try {
            Query q = em.createQuery("select t from ToDoAction as t where t.home.id = '" + homeId + "'");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
