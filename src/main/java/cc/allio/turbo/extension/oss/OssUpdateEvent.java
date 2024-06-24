package cc.allio.turbo.extension.oss;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OssUpdateEvent extends ApplicationEvent {

    private final String ossType;
    private final OssTrait ossTrait;

    public OssUpdateEvent(Object source, String ossType, OssTrait ossTrait) {
        super(source);
        this.ossType = ossType;
        this.ossTrait = ossTrait;
    }
}
