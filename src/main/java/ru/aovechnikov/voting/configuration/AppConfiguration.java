package ru.aovechnikov.voting.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created rootContext {@link ApplicationContext}  that is
 * used by the Spring IoC container as a source of bean definitions for service layer.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

@Configuration
@ComponentScan(basePackages = {"ru.aovechnikov.voting.service"})
@Import(SecurityConfig.class)
public class AppConfiguration {

}
