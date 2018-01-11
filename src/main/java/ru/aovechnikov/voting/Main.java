package ru.aovechnikov.voting;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.aovechnikov.voting.configuration.AppConfiguration;
import ru.aovechnikov.voting.configuration.DbConfiguration;
import ru.aovechnikov.voting.repository.RestaurantRepository;

import java.util.Arrays;

/**
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext appCtx = new AnnotationConfigApplicationContext(AppConfiguration.class, DbConfiguration.class);
        System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

        RestaurantRepository repository = appCtx.getBean(RestaurantRepository.class);
        repository.findAll().stream().forEach(System.out::println);
        appCtx.close();
    }
}
