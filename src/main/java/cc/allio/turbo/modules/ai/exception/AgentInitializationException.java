package cc.allio.turbo.modules.ai.exception;

public class AgentInitializationException extends Exception {

    public AgentInitializationException(String message) {
        super(message);
    }

    public AgentInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentInitializationException(Throwable cause) {
        super(cause);
    }

    public AgentInitializationException() {
        super();
    }
}
