package ru.aovechnikov.voting.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created rootContext {@link ApplicationContext}  that is
 * used by the Spring IoC container as a source of bean definitions for service layer.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

@Configuration
@ComponentScan(basePackages = {"ru.aovechnikov.voting.service", "ru.aovechnikov.voting.util.message" })
@Import(SecurityConfig.class)
public class AppConfiguration {

    /**
     * Accesses to resource bundles 'app'.
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(6);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("classpath:/messages/app");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

}
