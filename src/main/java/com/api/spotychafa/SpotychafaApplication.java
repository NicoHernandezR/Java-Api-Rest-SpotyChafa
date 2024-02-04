package com.api.spotychafa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpotychafaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotychafaApplication.class, args);
    }

    @Configuration
    public class MvcConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/songs/**")
                    .addResourceLocations("file:src/main/resources/songs/");
        }
    }
}
