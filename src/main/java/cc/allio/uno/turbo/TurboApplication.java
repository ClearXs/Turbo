package cc.allio.uno.turbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TurboApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurboApplication.class, args);
    }

    @RestController
    public static class Demo {

        @GetMapping("/hello")
        public String hello() {
            return "hello world";
        }
    }

}
