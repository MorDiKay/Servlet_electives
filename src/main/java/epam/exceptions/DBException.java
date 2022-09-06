package epam.exceptions;

/**
 * An exception that provides information on a database access error.
 *
 */
public class DBException extends ApplicationException {

    private static final long serialVersionUID = -3550446897536410392L;

    public DBException() {
        super();
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

}
