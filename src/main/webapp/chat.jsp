<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="navbar.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Conversation</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <style>
        body {
            background-color: #f1f3f6;
            margin: 0;
        }

        .chat-header {
            background: white;
            padding: 10px 15px;
            border-bottom: 1px solid #ddd;
            display: flex;
            align-items: center;
        }

        .chat-header h6 {
            margin: 0;
            font-weight: bold;
        }

        .back-btn {
            font-size: 20px;
            margin-right: 15px;
            color: #0d6efd;
            cursor: pointer;
        }

        /* Zone messages */
        .chat-container {
            padding: 20px;
            overflow-y: auto;
            height: calc(100vh - 140px); /* adapte selon hauteur navbar + header */
        }

        .message {
            max-width: 60%;
            padding: 10px 15px;
            border-radius: 15px;
            margin-bottom: 10px;
            position: relative;
            font-size: 14px;
        }

        .sent {
            background-color: #0d6efd;
            color: white;
            margin-left: auto;
            border-bottom-right-radius: 5px;
        }

        .received {
            background-color: #e9ecef;
            color: black;
            margin-right: auto;
            border-bottom-left-radius: 5px;
        }

        .message-time {
            font-size: 11px;
            opacity: 0.7;
            margin-top: 5px;
            text-align: right;
        }

        /* Zone input */
        .chat-input {
            position: fixed;
            bottom: 0;
            left: 0;
            width: 100%;
            background: white;
            padding: 10px 15px;
            border-top: 1px solid #ddd;
        }

        .send-btn {
            background-color: #0d6efd;
            color: white;
            border-radius: 50%;
            width: 45px;
            height: 45px;
            border: none;
        }

        .send-btn:hover {
            background-color: #0b5ed7;
        }
    </style>
</head>
<body>

<!-- Header conversation -->
<div class="chat-header">
    <a href="${pageContext.request.contextPath}/home" class="back-btn">
        <i class="bi bi-arrow-left"></i>
    </a>
    <h6>
        ${contact.firstName} ${contact.lastName}
    </h6>
</div>

<!-- Messages -->
<div class="chat-container" id="chatBox">

    <c:forEach var="msg" items="${messages}">

        <c:choose>

            <c:when test="${msg.sender.id == sessionScope.user.id}">
                <div class="message sent">
                        ${msg.content}
                    <div class="message-time">
                        <fmt:formatDate value="${msg.date_time}" pattern="dd/MM/yyyy HH:mm" />
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="message received">
                        ${msg.content}
                    <div class="message-time">
                        <fmt:formatDate value="${msg.date_time}" pattern="dd/MM/yyyy HH:mm" />
                    </div>
                </div>
            </c:otherwise>

        </c:choose>

    </c:forEach>

</div>

<!-- Input -->
<div class="chat-input">
    <form class="d-flex" onsubmit="sendMessage(); return false;">
        <input type="text" id="messageInput" class="form-control me-2" placeholder="Écrire un message..." required>
        <button onclick="sendMessage()" class="btn btn-primary">
            <i class="bi bi-send"></i>
        </button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function scrollToBottom() {
        const chatContainer = document.querySelector(".chat-container");
        chatContainer.scrollTop = chatContainer.scrollHeight;
    }

    window.onload = scrollToBottom;
</script>
<script>
    const userId = ${sessionScope.user.id};
    const contactId = ${contact.id};

    const socket = new WebSocket("ws://" + window.location.host + "${pageContext.request.contextPath}/ws/" + userId);

    socket.onmessage = function (event) {

        const parts = event.data.split("|");

        const senderId = parts[0];
        const content = parts[1];
        const date = parts[2];

        const chatBox = document.getElementById("chatBox");

        const messageDiv = document.createElement("div");
        messageDiv.classList.add("message");

        if (senderId == userId) {
            messageDiv.classList.add("sent");
        } else {
            messageDiv.classList.add("received");
        }

        messageDiv.innerHTML =
            content +
            "<div class='message-time'>" + date + "</div>";

        chatBox.appendChild(messageDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
    };

    function sendMessage() {

        event.preventDefault(); // empêche refresh

        const input = document.getElementById("messageInput");
        const content = input.value;

        if (content.trim() !== "") {

            socket.send(contactId + ":" + content);

            input.value = "";
        }
    }

</script>
</body>
</html>
