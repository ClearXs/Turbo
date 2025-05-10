package cc.allio.turbo.modules.ai.exception;

public class NotFoundPromptException extends RuntimeException {

    public NotFoundPromptException(String message) {
        super(message);
    }

    public NotFoundPromptException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundPromptException(Throwable cause) {
        super(cause);
    }

    public NotFoundPromptException() {
        super();
    }
}
