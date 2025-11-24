package com.Hajuuu.page.web;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new LoginMemberArgumentResolver());
//    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(new LogInterceptor())
////                .order(1)
////                .addPathPatterns("/**")
////                .excludePathPatterns("/css/*", "/assets/**", "/js/*",
////                        "/style.css", "/*.ico", "/error");
//
//        registry.addInterceptor(new LoginCheckInterceptor())

    /// /s                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/", "/login", "/logout", "/join", "/css/*", "/assets/**", "/js/*",
//                        "/style.css", "/*.ico", "/error");
//    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }

}
