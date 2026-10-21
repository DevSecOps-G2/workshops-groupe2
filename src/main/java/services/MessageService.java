package services;

import dao.MessageDAO;
import dao.UserDAO;
import model.Message;
import model.User;
import java.util.Date;
import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO = new MessageDAO();
    private final UserDAO userDAO = new UserDAO();

    public boolean sendMessage(Long senderId, Long receiverId, String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        User sender = userDAO.findById(senderId);
        User receiver = userDAO.findById(receiverId);

        if (sender == null || receiver == null) {
            return false;
        }

        Message message = new Message(sender, receiver, content, new Date());
        messageDAO.save(message);
        return true;
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        User user1 = userDAO.findById(userId1);
        User user2 = userDAO.findById(userId2);

        if (user1 == null || user2 == null) {
            return List.of(); // Liste vide
        }

        return messageDAO.findConversation(user1, user2);
    }

    public List<Message> getReceivedMessages(Long userId) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return List.of();
        }
        return messageDAO.findReceivedMessages(user);
    }

    public List<Message> getSentMessages(Long userId) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return List.of();
        }
        return messageDAO.findSentMessages(user);
    }

    public void markMessageAsRead(Long messageId) {
        messageDAO.markAsRead(messageId);
    }

    public int getUnreadCount(Long userId) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return 0;
        }
        return messageDAO.countUnreadMessages(user);
    }
}