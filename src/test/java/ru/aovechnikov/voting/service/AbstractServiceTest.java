package ru.aovechnikov.voting.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.aovechnikov.voting.configuration.AppConfiguration;
import ru.aovechnikov.voting.configuration.DbConfiguration;
import ru.aovechnikov.voting.repository.JpaUtil;
import ru.aovechnikov.voting.util.ValidationUtil;

import java.util.Collection;

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

    @Autowired
    protected JpaUtil jpaUtil;

    static {
        SLF4JBridgeHandler.install();
    }

    @Before
    public void setUp() throws Exception{
        configureAuthentication("ROLE_ADMIN");
        jpaUtil.clear2ndLevelHibernateCache();
    }

    public static void configureAuthentication(String role) {
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", role, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
