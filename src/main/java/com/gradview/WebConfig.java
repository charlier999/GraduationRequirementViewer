package com.gradview;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer 
{

    @Override
    public void addCorsMappings(CorsRegistry registry) 
    {
        // all other maps
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT")
                .allowedHeaders("*")
                .allowCredentials(false);
        // Ajax maps
        // registry.addMapping("/ajax/**")
        //         .allowedOrigins("http://localhost:8080")
        //         .allowedMethods("*")
        //         .allowedHeaders("*")
        //         .allowCredentials(false);
    }
}

