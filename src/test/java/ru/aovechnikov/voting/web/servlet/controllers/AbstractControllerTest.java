package ru.aovechnikov.voting.web.servlet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.aovechnikov.voting.configuration.AppConfiguration;
import ru.aovechnikov.voting.configuration.DbConfiguration;
import ru.aovechnikov.voting.configuration.WebMvcConfiguration;
import ru.aovechnikov.voting.web.servlet.json.JacksonObjectMapper;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Base class for testing controllers. Loads and configures an {@link WebApplicationContext} for integration tests and setup {@link MockMvc}.
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
@ContextConfiguration(classes = {AppConfiguration.class, DbConfiguration.class, WebMvcConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
abstract public class AbstractControllerTest {
    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    protected static ObjectMapper mapper = JacksonObjectMapper.getMapper();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }
}