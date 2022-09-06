package epam.servlets.commands;

import epam.db.MenuDao;
import epam.db.UserDao;
import epam.db.entities.Elective;
import epam.Path;

import epam.db.entities.Topic;
import epam.exceptions.ApplicationException;
import epam.exceptions.DBException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Lists electives of programming topic.
 * <p>
 * ElectiveProgramming command.
 */
public class ElectiveProgramming extends Command {

    private static final long serialVersionUID = 1863978254689588513L;

    private static final Logger log = Logger.getLogger(ElectiveProgramming.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {
        log.debug("Command starts");

        //Get electives list on a programming topic from a DataBase
        List<Elective> electives = new MenuDao().findElectivesByTopic(Topic.PROGRAMMING);

        HttpSession session = request.getSession();

        String errorMessage = null;
        String forward = Path.PAGE__ERROR_PAGE;

        //Condition for checking the list of electives for elements
        if (electives == null || electives.isEmpty()) {
            errorMessage = "Electives cannot be empty.";
            request.setAttribute("errorMessage", errorMessage);
            log.error("electives" + electives);
            return forward;
        }

        //Sets lecturer login to display it instead lecturer_id
        for (Elective e : electives) {
            e.setLecturerLogin(new UserDao().findUserById((long) e.getLecturerId()).getLogin());
        }

        String sort = request.getParameter("sort");
        String page = request.getParameter("page");
        String expected = request.getParameter("expected");

        //Condition to set programming topic to session
        if (Topic.getTopic(electives.get(0)) == Topic.PROGRAMMING)
            session.setAttribute("topic", "Programming");
        log.trace("Put to the session attribute topic --> " + Topic.PROGRAMMING);

        // Use a private method to select a number of page
        pagination(request, electives, page);

        //Condition to display courses that will be in the future
        if ("true".equals(expected)) {
            List<Elective> electives3 = new ArrayList<>();
            for (Elective elective : electives) {
                if (elective.getStatus().equals("expected"))
                    electives3.add(elective);
            }
            request.setAttribute("electives", electives3);
            log.trace("Display future electives --> " + electives3);
        }
        //Condition to display sorted electives list by end date
        if ("endDate".equals(sort) && page != null) {
            log.trace("Sorting by electives endDate...");
            Collections.sort(electives, new Comparator<Elective>() {
                @Override
                public int compare(Elective o1, Elective o2) {
                    return (int) (o1.getEndDate().compareTo(o2.getEndDate()));
                }
            });
            pagination(request, electives, page);
        } else if ("startDate".equals(sort) && page != null) { //Condition to display sorted electives list
            log.trace("Sorting by electives startDate...");    //by start date
            Collections.sort(electives, new Comparator<Elective>() {
                @Override
                public int compare(Elective o1, Elective o2) {
                    return (int) (o1.getStartDate().compareTo(o2.getStartDate()));
                }
            });
            pagination(request, electives, page);
        } else if ("name".equals(sort) && page != null) {   // Condition to display sorted electives list by name
            log.trace("Sorting by electives name...");
            Collections.sort(electives, new Comparator<Elective>() {
                @Override
                public int compare(Elective o1, Elective o2) {
                    return (int) (o1.getName().compareTo(o2.getName()));
                }
            });
            pagination(request, electives, page);
        } else if ("duration".equals(sort) && page != null) {  //Condition to display sorted electives list
            log.trace("Sorting by electives duration...");     // by duration
            Collections.sort(electives, new Comparator<Elective>() {
                @Override
                public int compare(Elective o1, Elective o2) {
                    long milliseconds_o1 = o1.getEndDate().getTime() - o1.getStartDate().getTime();
                    int days_o1 = (int) (milliseconds_o1 / (24 * 60 * 60 * 1000));
                    long milliseconds_o2 = o2.getEndDate().getTime() - o2.getStartDate().getTime();
                    int days_o2 = (int) (milliseconds_o2 / (24 * 60 * 60 * 1000));
                    return (int) (days_o1 - days_o2);
                }
            });
            pagination(request, electives, page);
        } else if ("count".equals(sort) && page != null) {    //Condition to display sorted electives list
            log.trace("Sorting by students count...");        // by students count
            Collections.sort(electives, new Comparator<Elective>() {
                @Override
                public int compare(Elective o1, Elective o2) {
                    return (int) (o1.getStudentCount() - o2.getStudentCount());
                }
            });
            pagination(request, electives, page);
        } else if ("lecturer".equals(sort) && page != null) {
            Collections.sort(electives, new Comparator<Elective>() {
                @Override
                public int compare(Elective o1, Elective o2) {
                    return (int) (o1.getLecturerId() - o2.getLecturerId());
                }
            });
            pagination(request, electives, page);
        }

        //Forward to desired page
        forward = Path.PAGE__PROGRAMMING_ELECTIVE;
        log.trace("Set the session attribute: electives -> " + electives);

        log.debug("Command finished");
        return forward;
    }

    //Helper function for displaying pages (pagination by arrays)
    private void pagination(HttpServletRequest request, List<Elective> electives, String page) {
        if ("1".equals(page)) {
            List<Elective> electives1 = new ArrayList<>();
            for (int i = 0; i < 3; i++)
                electives1.add(i, electives.get(i));
            request.setAttribute("electives", electives1);
        } else if ("2".equals(page)) {
            List<Elective> electives2 = new ArrayList<>();
            for (int i = 3; i < electives.size(); i++)
                electives2.add(i - 3, electives.get(i));
            request.setAttribute("electives", electives2);
        }
    }

}
