package ru.aovechnikov.voting.web.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.DefaultRelProvider;
import org.springframework.hateoas.hal.Jackson2HalModule;

/**
 * Configures the halObjectMapper to render {@link Link} and {@link ResourceSupport} instances
 * in HAL compatible JSON.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
public class JacksonObjectMapper extends ObjectMapper{

    private static final ObjectMapper MAPPER = new JacksonObjectMapper();

    public static ObjectMapper getMapper() {
        return MAPPER;
    }
    /**
     * Configure the halObjectMapper
     */
    private JacksonObjectMapper() {
        registerModule(new Hibernate5Module());
        registerModule(new JavaTimeModule());

        registerModule(new Jackson2HalModule());
        setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(new DefaultRelProvider(), null, null));
        enable(SerializationFeature.INDENT_OUTPUT);
        configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);


        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
