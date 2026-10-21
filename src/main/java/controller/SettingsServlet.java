package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import services.UserService;
import util.PasswordUtil;

import java.io.IOException;

@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("/settings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        // Vérifier mot de passe actuel
        if (!PasswordUtil.checkPassword(currentPassword, currentUser.getPassword())) {

            request.setAttribute("error", "Mot de passe actuel incorrect");
            request.getRequestDispatcher("/settings.jsp").forward(request, response);
            return;
        }

        // Hash nouveau mot de passe
        String hashedPassword = PasswordUtil.hashPassword(newPassword);

        currentUser.setPassword(hashedPassword);
        userService.updateUser(currentUser);

        // Mise à jour session
        session.setAttribute("user", currentUser);

        request.setAttribute("success", "Mot de passe mis à jour avec succès");
        request.getRequestDispatcher("/settings.jsp").forward(request, response);
    }
}
