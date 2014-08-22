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
import models.Home;
import models.Room;
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
        List<Room> rooms = h.getRooms();
        RoomDao daoRoom = new RoomDao();
        DeviceDao daoDevice = new DeviceDao();
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).setHome(h);
            Room r = daoRoom.get(rooms.get(i));
            if (r != null) {
                rooms.get(i).setId(r.getId());
            }
            List<Device> devices = rooms.get(i).getDevices();
            for (int j = 0; j < devices.size(); j++) {
                devices.get(j).setRoom(rooms.get(i));
                Device d = daoDevice.get(devices.get(j));
                if (d != null) {
                    devices.get(j).setId(d.getId());
                }
            }
        }

            try {
                if (this.get(h.getId()) == null) {
                    em.persist(h);
                } else {
                    em.merge(h);
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
