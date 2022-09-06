package epam.exceptions;

/**
 * An exception that provides information on an application error.
 *
 */
 public class ApplicationException extends Exception {

    private static final long serialVersionUID = 8288779062647218916L;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
