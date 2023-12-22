package cc.allio.turbo.extension.oss;

import org.springframework.context.ApplicationListener;

public class OssUpdateListener implements ApplicationListener<OssUpdateEvent> {

    @Override
    public void onApplicationEvent(OssUpdateEvent event) {
        OssExecutorFactory.toggleExecutor(event.getOssType(), event.getOssTrait());
    }
}
