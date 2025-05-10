package cc.allio.turbo.modules.ai.exception;

public class ResourceParseException extends Exception {

    public ResourceParseException(String message) {
        super(message);
    }

    public ResourceParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceParseException(Throwable cause) {
        super(cause);
    }

    public ResourceParseException() {
        super();
    }
}
