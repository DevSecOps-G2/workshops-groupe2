package controller;

import services.MessageService;
import services.UserService;
import model.Message;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/chat")
public class MessageServlet extends HttpServlet {

    private MessageService messageService = new MessageService();
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String contactId = request.getParameter("contactId");

        if (contactId != null) {

            Long contactUserId = Long.parseLong(contactId);

            User contact = userService.getUserById(contactUserId);

            List<Message> conversation =
                    messageService.getConversation(currentUser.getId(), contactUserId);

            request.setAttribute("contact", contact);
            request.setAttribute("messages", conversation);
        }

        request.getRequestDispatcher("/chat.jsp").forward(request, response);
    }
}
