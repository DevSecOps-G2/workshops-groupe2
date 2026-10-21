package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import model.User;
import util.JpaUtil;

import java.util.List;

public class UserDAO {

    public void save(User user) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
    }

    public User findById(Long id) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            return em.find(User.class, id);
        }
    }

    public User findByEmail(String email) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<User> findAll() {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u ORDER BY u.firstName", User.class);
            return query.getResultList();
        }
    }

    public List<User> findAllExcept(Long userId) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.id != :userId ORDER BY u.firstName",
                    User.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
    }

    public void update(User user) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        }
    }

    public void delete(Long id) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        }
    }
}
