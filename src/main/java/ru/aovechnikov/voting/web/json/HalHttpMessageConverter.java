package ru.aovechnikov.voting.web.json;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import static ru.aovechnikov.voting.web.json.JacksonObjectMapper.getMapper;

/**
 * Implementing {@link HttpMessageConverter} for convert {@link MediaTypes#HAL_JSON_UTF8_VALUE} and {@link MediaType#APPLICATION_JSON}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@Component
public class HalHttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public HalHttpMessageConverter() {
        super(getMapper(), new MediaType("application", "hal+json", DEFAULT_CHARSET), MediaType.APPLICATION_JSON);
    }
}
