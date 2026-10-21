package services;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public boolean register(String firstName, String lastName, String email,
                            String password, String phone) {
        if (userDAO.findByEmail(email) != null) {
            return false; // Email déjà utilisé
        }

        User user = new User(firstName, lastName, email,
                PasswordUtil.hashPassword(password), phone);
        userDAO.save(user);
        return true;
    }

    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getAllUsersExcept(Long userId) {
        return userDAO.findAllExcept(userId);
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }
}