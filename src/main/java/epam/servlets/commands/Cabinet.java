package epam.servlets.commands;

import org.apache.log4j.Logger;

import epam.db.UserDao;
import epam.exceptions.ApplicationException;

import epam.db.MenuDao;
import epam.db.entities.Elective;
import epam.db.entities.User;
import epam.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

/**
 * Command for getting to the personal account page
 */
public class Cabinet extends Command {

    private static final Logger log = Logger.getLogger(Login.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {
        log.debug("Command starts");

        HttpSession session = request.getSession();

        String errorMessage;
        String forward = Path.PAGE__ERROR_PAGE;

        User user = (User) session.getAttribute("user");

        int user_id = user.getId();

        String[] check = null;

        if (request.getParameterValues("checkItem") != null) {
            check = request.getParameterValues("checkItem");

            List<Integer> electivesId = new MenuDao().findElectivesIdByUserId(user_id);

            for (int i = 0; i < electivesId.size(); i++) {
                for (String s : check) {
                    if (electivesId.get(i) == Integer.parseInt(s)) {
                        errorMessage = "You have selected a course for which you have already enrolled.";
                        session.setAttribute("errorMessage", errorMessage);
                        session.setAttribute("home", "home");
                        return forward;
                    }
                }
            }
            for (String s : check) {
                MenuDao.insertElectivesHaveUsers(user_id, Integer.parseInt(s));
                MenuDao.updateElectiveByStudentCount(Integer.parseInt(s), true);
            }
            log.trace("Insert to database list of selected courses --> " + Arrays.toString(check));
        }

        if (request.getParameterValues("checkItemDelete") != null) {
            check = request.getParameterValues("checkItemDelete");
            for (String s : check) {
                MenuDao.updateElectiveByStudentCount(Integer.parseInt(s), false);
                MenuDao.deleteElectivesHaveUsers(user_id, Integer.parseInt(s));
            }
            log.trace("Insert to database list of selected courses --> " + Arrays.toString(check));
        }

        List<Integer> electives_id = new MenuDao().findElectivesIdByUserId(user_id);
        String[] electives_id_string = null;
        if (electives_id != null) {
            electives_id_string = new String[electives_id.size()];
            for (int i = 0; i < electives_id.size(); i++)
                electives_id_string[i] = String.valueOf(electives_id.get(i));
        }

        List<Elective> electiveList = new MenuDao().findElectivesByIdsAndUserId();

        if (electiveList == null) {
            errorMessage = "List of electives is empty, please, make your choice";
            session.setAttribute("errorMessage", errorMessage);
            session.setAttribute("home", "home");
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }

        for (Elective e : electiveList) {
            e.setLecturerLogin(new UserDao().findUserById((long) e.getLecturerId()).getLogin());
        }

        session.setAttribute("electives", electiveList);
        log.trace("Put electives list to the session --> " + electiveList);

        forward = Path.PAGE__HOME;
        return forward;
    }
}
