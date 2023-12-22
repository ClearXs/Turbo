package cc.allio.turbo.extension.oss;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class OssUpdateEvent extends ApplicationEvent {

    @Getter
    private final String ossType;
    @Getter
    private final OssTrait ossTrait;

    public OssUpdateEvent(Object source, String ossType, OssTrait ossTrait) {
        super(source);
        this.ossType = ossType;
        this.ossTrait = ossTrait;
    }
}
