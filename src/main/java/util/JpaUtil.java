package util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("LiveChat");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
