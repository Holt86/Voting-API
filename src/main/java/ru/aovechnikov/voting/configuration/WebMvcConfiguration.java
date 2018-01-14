package ru.aovechnikov.voting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.aovechnikov.voting.web.json.HalHttpMessageConverter;

import java.util.List;

/**
 * Created mvcContext {@link WebApplicationContext}  via {@code @EnableWebMvc}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */

@EnableWebMvc
@Configuration
@EnableSpringDataWebSupport
@ComponentScan(basePackages = {"ru.aovechnikov.voting.web"})
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private HalHttpMessageConverter halHttpMessageConverter;

    /**
     Views static resources
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * Add handlers to serve static resources.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    /**
     * Add resolvers to support the {@link Authentication#getPrincipal()} using the
     * {@link AuthenticationPrincipal} annotation.
     *
     * @param resolvers initially an empty list.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    /**
     * Configure the {@link HalHttpMessageConverter}.
     * *
     * @param converters initially an empty list of converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(halHttpMessageConverter);
    }
}
