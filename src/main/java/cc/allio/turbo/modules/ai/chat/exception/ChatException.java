package cc.allio.turbo.modules.ai.chat.exception;

public class ChatException extends Exception {

    public ChatException() {
        super();
    }

    public ChatException(String message) {
        super(message);
    }

    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatException(Throwable cause) {
        super(cause);
    }

    protected ChatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
