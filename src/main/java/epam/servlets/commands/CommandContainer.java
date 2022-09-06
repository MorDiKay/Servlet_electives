package epam.servlets.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Container for all commands.
 */
public class CommandContainer {

    private static final Logger log = Logger.getLogger(CommandContainer.class);

    private static Map<String, Command> commands = new HashMap<>();

    static {
        // common commands
        commands.put("login", new Login());
        commands.put("logout", new Logout());
        commands.put("electiveSport", new ElectiveSport());
        commands.put("electiveProgramming", new ElectiveProgramming());
        commands.put("cabinet", new Cabinet());
        commands.put("noCommand", new NoCommand());
        commands.put("contact", new Contact());

        // client command
        commands.put("listTopics", new ListTopics());

        // admin command
        commands.put("listElectives", new ListElectives());

        // lecturer command
        commands.put("journal", new Journal());

        log.debug("Command container was successfully initialized");
        log.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName Name of the command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            log.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }

}