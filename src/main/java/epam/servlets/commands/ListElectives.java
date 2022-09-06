package epam.servlets.commands;

import org.apache.log4j.Logger;

import epam.exceptions.ApplicationException;
import epam.db.UserDao;
import epam.db.entities.User;

import epam.db.MenuDao;
import epam.db.bean.UserOrderBean;
import epam.db.entities.Elective;
import epam.db.entities.Topic;
import epam.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ListElectives command (admin page).
 *
 */
public class ListElectives extends Command {

    private static final long serialVersionUID = 1963978254689586513L;

    private static final Logger log = Logger.getLogger(ListElectives.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {
        log.debug("Command starts");

        String errorMessage;
        String forward = Path.PAGE__ERROR_PAGE;
        Integer lecturerId = null;
        String electiveId = null;
        String lecturerLogin = null;

        if (request.getParameter("lecturerList") != null) {
            electiveId = request.getParameter("electiveId");
            lecturerLogin = request.getParameter("lecturerList");
            User lecturer = new UserDao().findUserByLogin(lecturerLogin);
            lecturerId = lecturer.getId();
            MenuDao.updateElectiveByLecturer(Integer.parseInt(electiveId), lecturerId);
        }

        //Get list of UserOrderBean entities
        List<UserOrderBean> userOrderBeanList = new MenuDao().findUserOrderBeans();

        //Iterate list of UserOrderBean entities to set lecturer login by lecturer id
        for (UserOrderBean bean : userOrderBeanList) {
            bean.setLecturerLogin(new UserDao().findUserById((long) bean.getLecturerId()).getLogin());
        }
        log.trace("Found in DB: userOrderBeanList --> " + userOrderBeanList);

        String[] check = null;

        // Checking for selected elements and putting them into an String array 'check' if they not null
        // After that delete this items from DB and userOrderBean list
        if (request.getParameterValues("checkItemDelete") != null) {
            check = request.getParameterValues("checkItemDelete");

            for (int i = 0; i < userOrderBeanList.size(); i++) {
                for (String s : check) {
                    if (Integer.parseInt(s) == userOrderBeanList.get(i).getElectiveId()) {
                        MenuDao.deleteElectivesHaveUsers(userOrderBeanList.get(i).getUserId(), Integer.parseInt(s));
                        MenuDao.updateElectiveByStudentCount(userOrderBeanList.get(i).getElectiveId(), false);
                        log.trace("Elective was deleted: " + userOrderBeanList.get(i));
                        userOrderBeanList.remove(i);
                    }
                }
            }
            log.trace("Deleted from database students and selected courses --> " + Arrays.toString(check));
        }

        //Get parameters to view electives by topics
        if ("true".equals(request.getParameter("viewSportElectives"))) {
            if (request.getParameterValues("electivesDelete") != null) {
                check = request.getParameterValues("electivesDelete");
                MenuDao.deleteElectivesByIds(check);
            }
            List<Elective> sportElectives = new MenuDao().findElectivesByTopic(Topic.SPORT);
            for (Elective e : sportElectives) {
                e.setLecturerLogin(new UserDao().findUserById((long) e.getLecturerId()).getLogin());
            }
            request.setAttribute("electives", sportElectives);
        }
        // Checking for a parameter 'viewProgrammingElectives' and displaying programming electives if true
        // After that checking for parameters 'electivesDelete' and putting them into an String array 'check' if they not null
        // After that delete objects from DB and select login of lecturer from 'lecturer_id'
        else if ("true".equals(request.getParameter("viewProgrammingElectives"))) {
            if (request.getParameterValues("electivesDelete") != null) {
                check = request.getParameterValues("electivesDelete");
                MenuDao.deleteElectivesByIds(check);
            }
            List<Elective> programmingElectives = new MenuDao().findElectivesByTopic(Topic.PROGRAMMING);
            for (Elective e : programmingElectives) {
                e.setLecturerLogin(new UserDao().findUserById((long) e.getLecturerId()).getLogin());
            }
            request.setAttribute("electives", programmingElectives);
        }
        // Checking for selected elements and putting them into an String array 'check' if they not null
        // After that change attribute 'is_blocked' of this items in DB and Users list
        else if ("true".equals(request.getParameter("viewAllStudents"))) {
            List<User> students = new UserDao().findUsersByRole(1);
            if (request.getParameterValues("blockStudent") != null) {
                check = request.getParameterValues("blockStudent");
                for (User user : students) {
                    for (String s : check) {
                        if (user.getLogin().equals(s)) {
                            if(user.isBlocked()) {
                                UserDao.updateUserByIsBlocked(s, false);
                                user.setBlocked(false);
                            } else {
                                UserDao.updateUserByIsBlocked(s, true);
                                user.setBlocked(true);
                            }
                        }
                    }
                }
            }
            request.setAttribute("students", students);
        }

        //Get list of lecturers for displaying them in a table to choose 1 of them
        List<User> lecturerList = new UserDao().findUsersByRole(2);
        request.setAttribute("lecturerList", lecturerList);

        // Sort list of UserOrderBean entities by electiveId
        if ("listElectives".equals(request.getParameter("command")))
            Collections.sort(userOrderBeanList, compareById);

        // Put sorted UserOrderBean list to request
        request.setAttribute("userOrderBeanList", userOrderBeanList);
        log.trace("Set the request attribute: userOrderBeanList --> " + userOrderBeanList);

        log.debug("Command finished");
        return Path.PAGE__LIST_ELECTIVES;
    }

    //Make the comparator a member of the class
    private static Comparator<UserOrderBean> compareById = new CompareById();

    //Implement Comparator interface and override compare method to compare UserOrderBeans by ElectiveId
    private static class CompareById implements Comparator<UserOrderBean>, Serializable {
        private static final long serialVersionUID = -1573481565177573283L;

        public int compare(UserOrderBean bean1, UserOrderBean bean2) {
            if (bean1.getElectiveId() > bean2.getElectiveId()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}