package exception;

public class TccSystemException extends RuntimeException {

    public TccSystemException(String message) {
        super(message);
    }

    public TccSystemException(Throwable e) {
        super(e);
    }

    public TccSystemException(String message, Throwable e) {
        super(message, e);
    }
}
