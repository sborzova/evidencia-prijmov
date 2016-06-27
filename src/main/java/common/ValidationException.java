package common;

/**
 * Created by Tomas on 27.06.2016.
 */
public class ValidationException extends RuntimeException {

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
