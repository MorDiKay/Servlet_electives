package epam.servlets.commands;

import org.apache.log4j.Logger;

import epam.exceptions.ApplicationException;

import epam.db.UserDao;
import epam.db.entities.User;
import epam.db.entities.Role;

import epam.Path;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import java.util.Date;

/**
 * Login command
 */
public class Login extends Command {

    private static final long serialVersionUID = -3071536593627692473L;

    private static final Logger log = Logger.getLogger(Login.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {
        log.debug("Command starts");

        HttpSession session = request.getSession();

        String login = request.getParameter("login");
        Cookie loginCookie = new Cookie("loginCookie", login);
        log.trace("get parameter login-->" + login);

        String password = request.getParameter("password");
        Cookie passwordCookie = new Cookie("passwordCookie", password);
        log.trace("get parameter password-->" + password);

        loginCookie.setMaxAge(2_629_743);
        passwordCookie.setMaxAge(2_629_743);

        String errorMessage = null;
        String forward = Path.PAGE__ERROR_PAGE;

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = "Login/password cannot be empty " +
                    "Please, enter your login and password!";
            session.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage -->" + errorMessage);
            return forward;
        }

        User user = new UserDao().findUserByLoginAndPassword(login, password);
        log.trace("Found in DB: user -> " + user);

        if (user == null) {
            errorMessage = "Cannot find user with such login/password. "
                    + "Сheck if your login/password is correct.";
            session.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }
        if (user.isBlocked()) {
            errorMessage = "You have been blocked by an administrator. " +
                    "To solve the problem and find out the reasons, contact technical support.";
            session.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }

        Role userRole = Role.getRole(user);
        if (userRole == Role.ADMIN)
            forward = Path.COMMAND__LIST_ELECTIVES;

        if (userRole == Role.CLIENT)
            forward = Path.COMMAND__LIST_TOPICS;

        if (userRole == Role.LECTURER)
            forward = Path.COMMAND__LIST_JOURNAL;

        session.setAttribute("user", user);
        session.setAttribute("userRole", userRole);

        Date date = new Date();
        session.setAttribute("date", date);
        log.trace("Set the session attribute: user -> " + user);
        log.trace("Set the session attribute: userRole -> " + userRole);
        log.trace("Set the session attribute: date -> " + date);

        log.debug("Command finished");
        return forward;
    }
}
