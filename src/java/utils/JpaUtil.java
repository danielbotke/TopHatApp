package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {
 
    private static JpaUtil instance;
    
    private EntityManagerFactory emf;
    
    private JpaUtil() {
    }
    
    public static JpaUtil get() {
        if (instance == null) {
            instance = new JpaUtil();
        } 
        return instance;
    }
    
    public EntityManager getEntityManager() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("TopHatAppPU");
        }
        return emf.createEntityManager();
    }
}
