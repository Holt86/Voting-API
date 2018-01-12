package ru.aovechnikov.voting.service;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.aovechnikov.voting.configuration.AppConfiguration;
import ru.aovechnikov.voting.configuration.DbConfiguration;
import ru.aovechnikov.voting.util.ValidationUtil;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Base class for testing service layer. Loads and configures an {@link ApplicationContext} for integration tests
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
@ContextConfiguration(classes = {AppConfiguration.class, DbConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
abstract public class AbstractServiceTest {

    public static Pageable pageable = PageRequest.of(0, 20, Sort.by("id"));

    static {
        SLF4JBridgeHandler.install();
    }

    public static <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        try {
            runnable.run();
            Assert.fail("Expected " + exceptionClass.getName());
        } catch (Exception e) {
            Assert.assertThat(ValidationUtil.getRootCause(e), instanceOf(exceptionClass));
        }
    }
}
