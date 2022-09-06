package epam.servlets.commands;

import epam.exceptions.ApplicationException;
import org.apache.log4j.Logger;

import epam.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Lists topics items.
 */
public class ListTopics extends Command {

    private static final long serialVersionUID = 7732286214029478505L;

    private static final Logger log = Logger.getLogger(ListTopics.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {
        log.debug("Command starts");

        HttpSession session = request.getSession();

        String errorMessage = null;
        String forward = Path.PAGE__ERROR_PAGE;

        if (session.getAttribute("user") == null) {
            errorMessage = "You are not logged.";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }

        forward = Path.PAGE__LIST_TOPICS;

        log.debug("Command finished");
        return forward;
    }

}