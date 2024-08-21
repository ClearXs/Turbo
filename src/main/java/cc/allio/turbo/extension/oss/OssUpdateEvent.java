package cc.allio.turbo.extension.oss;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OssUpdateEvent extends ApplicationEvent {

    private final OssTrait ossTrait;

    public OssUpdateEvent(Object source,OssTrait ossTrait) {
        super(source);
        this.ossTrait = ossTrait;
    }
}
