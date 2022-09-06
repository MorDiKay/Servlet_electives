package epam.servlets;

import epam.exceptions.ApplicationException;
import epam.exceptions.DBException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import epam.servlets.commands.Command;
import epam.servlets.commands.CommandContainer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Main servlet controller.
 *
 */

public class Controller extends HttpServlet {

    //private static final long serialVersionUID = 2423353715955164816L;

    private static final Logger log = Logger.getLogger(Controller.class);

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Forward to the desired page
        try {
            getServletContext().getRequestDispatcher(process(request, response)).forward(request, response);
        } catch (ApplicationException ex) {
            log.error(ex.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

       /* Using PRG Pattern.
        * Instead of forwarding from doPost() method, we are doing
        * redirection to avoid duplicate form submission.
        */
        try {
            response.sendRedirect(process(request, response));
        } catch (ApplicationException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Main method of this controller.
     */
    private String process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ApplicationException {

        log.debug("Controller starts");

        // extract command name from the request
        String commandName = request.getParameter("command");
        log.trace("Request parameter: command --> " + commandName);


        // obtain command object by its name
        Command command = CommandContainer.get(commandName);
        log.trace("Obtained command --> " + command);


        // execute command and get forward address
        String forward = command.execute(request, response);
        log.trace("Forward address --> " + forward);

        log.debug("Controller finished, now go to forward address --> " + forward);
        return forward;
    }

}
