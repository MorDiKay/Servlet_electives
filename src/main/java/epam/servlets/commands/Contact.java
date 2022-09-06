package epam.servlets.commands;

import epam.Path;
import epam.exceptions.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Contact command
 */
public class Contact extends Command {

    private static Logger log = Logger.getLogger(Contact.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {
        log.debug("Command starts");

        HttpSession session = request.getSession();

        String errorMessage;
        String forward = Path.PAGE__ERROR_PAGE;

        // Get content of parameters in ServletContext (web.xml)
        String contact = request.getServletContext().getInitParameter("Contact");

        if (contact == null) {
            errorMessage = "Cannot get Servlet Context 'Contact'";
            session.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }

        request.setAttribute("contact", contact);
        log.trace("Put string from servlet context to request parameters --> " + contact);

        forward = Path.PAGE__CONTACT;
        log.debug("Command ends");
        return forward;
    }
}
