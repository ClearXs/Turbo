package cc.allio.turbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cc.allio.turbo.**", exclude = {ZipkinAutoConfiguration.class})
public class TurboApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurboApplication.class, args);
    }

}
