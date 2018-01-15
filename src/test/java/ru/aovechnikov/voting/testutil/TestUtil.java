package ru.aovechnikov.voting.testutil;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.aovechnikov.voting.model.User;

import java.io.UnsupportedEncodingException;

/**
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class TestUtil {

    public static final Pageable PAGEABLE = PageRequest.of(0, 20, Sort.by("id"));

    public static String getContent(ResultActions actions) throws UnsupportedEncodingException {
        return actions.andReturn().getResponse().getContentAsString();
    }

    /**
     * Sets the Authorization header to use HTTP Basic with
     * the username and password of {@code user}.
     */
    public static RequestPostProcessor httpBasic(User user){
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    /**
     * Gets id of entity by {@code jsonPathLink}
     */
    public static int getIdFromLink(ResultActions actions, String jsonPathLink)throws UnsupportedEncodingException{
        DocumentContext context = JsonPath.parse(getContent(actions));
        String link = context.read(jsonPathLink).toString();
        int id = Integer.valueOf(link.substring(link.lastIndexOf("/") + 1));
        return id;
    }
}
