package cc.allio.turbo.common.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Knife4jConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger静态资源放行
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("system")
                .displayName("系统模块")
                .pathsToMatch("/sys/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .displayName("认证模块")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi developerApi() {
        return GroupedOpenApi.builder()
                .group("developer")
                .displayName("开发者模块")
                .pathsToMatch("/developer/**")
                .build();
    }

    @Bean
    public GroupedOpenApi messageApi() {
        return GroupedOpenApi.builder()
                .group("message")
                .displayName("消息模块")
                .pathsToMatch("/message/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setName("j.x");
        contact.setEmail("jiangw1027@gmail.com");
        contact.setUrl("https://github.com/ClearXs/Turbo");
        Info info = new Info()
                .title("Turbo快速开发平台")
                .description("基于springboot3.x，拥有多租户、鉴权、用户菜单、工作流、表单的快速开发平台")
                .contact(contact)
                .version("1.0");
        return new OpenAPI().info(info);
    }

}
