package cc.allio.turbo.extension.ob.log;

import cc.allio.turbo.extension.ob.opentelemetry.OpenTelemetryAutoConfiguration;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.junit.jupiter.api.Test;

@RunTest(active = "openobserve", components = {TurboLogAutoConfiguration.class, OpenTelemetryAutoConfiguration.class})
public class PrinterTest {

    @Inject
    private OpenTelemetry openTelemetry;

    @Test
    void testSl4j() throws InterruptedException {
        OpenTelemetryAppender.install(openTelemetry);

        Printer.print("test");

        Thread.sleep(10000);
    }
}
