package controller;

import services.UserService;
import services.MessageService;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private UserService userService = new UserService();
    private MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        List<User> contacts = userService.getAllUsersExcept(currentUser.getId());
        int unreadCount = messageService.getUnreadCount(currentUser.getId());

        request.setAttribute("contacts", contacts);
        request.setAttribute("unreadCount", unreadCount);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
