package dao;

import model.Message;
import model.User;
import util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MessageDAO {

    public void save(Message message) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(message);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Message findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Message.class, id);
        } finally {
            em.close();
        }
    }

    public List<Message> findConversation(User user1, User user2) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Message> query = em.createQuery(
                    "SELECT m FROM Message m WHERE " +
                            "(m.sender = :user1 AND m.receiver = :user2) OR " +
                            "(m.sender = :user2 AND m.receiver = :user1) " +
                            "ORDER BY m.date_time ASC", Message.class);
            query.setParameter("user1", user1);
            query.setParameter("user2", user2);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Message> findReceivedMessages(User receiver) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Message> query = em.createQuery(
                    "SELECT m FROM Message m WHERE m.receiver = :receiver " +
                            "ORDER BY m.date_time DESC", Message.class);
            query.setParameter("receiver", receiver);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Message> findSentMessages(User sender) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Message> query = em.createQuery(
                    "SELECT m FROM Message m WHERE m.sender = :sender " +
                            "ORDER BY m.date_time DESC", Message.class);
            query.setParameter("sender", sender);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void markAsRead(Long messageId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Message message = em.find(Message.class, messageId);
            if (message != null) {
                message.setIsRead(true);
                em.merge(message);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public int countUnreadMessages(User receiver) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(m) FROM Message m WHERE m.receiver = :receiver AND m.isRead = false",
                    Long.class);
            query.setParameter("receiver", receiver);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}