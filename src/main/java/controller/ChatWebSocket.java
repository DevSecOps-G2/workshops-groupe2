package controller;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import model.User;
import services.MessageService;
import services.UserService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/{userId}")
public class ChatWebSocket {

    private static Map<Long, Session> sessions = new ConcurrentHashMap<>();

    private static MessageService messageService = new MessageService();
    private static UserService userService = new UserService();

    private Long currentUserId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        currentUserId = userId;
        sessions.put(userId, session);
        System.out.println("User connecté : " + userId);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        // format reçu : receiverId:content
        String[] parts = message.split(":", 2);
        Long receiverId = Long.parseLong(parts[0]);
        String content = parts[1];

        // 1️⃣ Sauvegarde en base
        boolean saved = messageService.sendMessage(currentUserId, receiverId, content);

        if (!saved) return;

        // 2️⃣ Récupérer le dernier message sauvegardé
        // (On récupère la conversation et on prend le dernier)
        var conversation = messageService.getConversation(currentUserId, receiverId);
        var lastMessage = conversation.get(conversation.size() - 1);

        // 3️⃣ Formater la date
        String formattedDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(lastMessage.getDate_time());

        // 4️⃣ Préparer message à envoyer
        // Format : senderId|content|date
        String fullMessage = currentUserId + "|" + content + "|" + formattedDate;

        // 5️⃣ Envoyer au receiver
        Session receiverSession = sessions.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.getBasicRemote().sendText(fullMessage);
        }

        // 6️⃣ Envoyer aussi au sender (pour affichage immédiat)
        Session senderSession = sessions.get(currentUserId);
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.getBasicRemote().sendText(fullMessage);
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(currentUserId);
        System.out.println("User déconnecté : " + currentUserId);
    }
}
