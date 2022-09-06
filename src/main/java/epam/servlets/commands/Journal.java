package epam.servlets.commands;

import epam.Path;
import epam.db.MenuDao;
import epam.db.UserDao;
import epam.db.bean.UserOrderBean;
import epam.db.entities.Temp;
import epam.db.entities.User;
import epam.exceptions.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Lists UserOrderBeans items.
 * <p>
 * Journal command (lecturer page).
 */
public class Journal extends Command {

    private static final long serialVersionUID = 1863973554689586513L;

    private static final Logger log = Logger.getLogger(ListElectives.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {
        log.debug("Command starts");

        //Get session from request (put in the Login)
        HttpSession session = request.getSession();

        //Get user entity from session
        User user = (User) session.getAttribute("user");

        //Take id of user (lecturer) to display electives taught by this lecturer
        Integer lecturerId = user.getId();

        String errorMessage;
        String forward = Path.PAGE__ERROR_PAGE;
        String mark = null;

        //Check parameter from request for selected objects
        if (request.getParameter("userMark") != null) {
            mark = request.getParameter("userMark");
            MenuDao.updateElectivesHaveUsers(Integer.parseInt(request.getParameter("userId")),
                    Integer.parseInt(request.getParameter("electiveId")), Integer.parseInt(mark));
        }

        //Get userOrderBean list from DB
        List<UserOrderBean> userOrderBeanList = new MenuDao().findUserOrderBeansByLecturerId(lecturerId);

        for (UserOrderBean bean : userOrderBeanList) {
            bean.setLecturerLogin(new UserDao().findUserById((long) bean.getLecturerId()).getLogin());
        }
        log.trace("Found in DB: userOrderBeanList --> " + userOrderBeanList);

        //NEW SQL REQUEST (TEMP)
        List<Temp> tempList = new MenuDao().temp(1);
        request.setAttribute("tempList", tempList);

        String[] check = null;

        if (request.getParameterValues("checkItemDelete") != null) {
            check = request.getParameterValues("checkItemDelete");
            for (int i = 0; i < check.length; i++) {
                if (Integer.parseInt(check[i]) == userOrderBeanList.get(i).getElectiveId()) {
                    MenuDao.deleteElectivesHaveUsers(userOrderBeanList.get(i).getUserId(), Integer.parseInt(check[i]));
                    userOrderBeanList.remove(i);
                }
            }
            log.trace("Insert to database list of selected courses --> " + Arrays.toString(check));
        }

        // Put sorted UserOrderBean list to request
        request.setAttribute("userOrderBeanList", userOrderBeanList);
        log.trace("Set the request attribute: userOrderBeanList --> " + userOrderBeanList);


        log.debug("Command finished");
        return Path.PAGE__LIST_JOURNAL;
    }
}