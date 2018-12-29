package com.cse.naruto.context.configurer;

import com.cse.naruto.context.interceptor.NarutoInterceptor;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class NarutoWebConfigurer implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NarutoInterceptor())
                .addPathPatterns("")
                .addPathPatterns("/")
                .addPathPatterns("/index")
                .addPathPatterns("/admin")
                .addPathPatterns("/material/**")
                .addPathPatterns("/api/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/material/edit").setViewName("material-edit");
    }
}
