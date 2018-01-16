package ru.aovechnikov.voting.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ru.aovechnikov.voting.util.exception.ErrorInfo;
import ru.aovechnikov.voting.util.exception.ErrorType;
import ru.aovechnikov.voting.util.message.MessageUtil;
import ru.aovechnikov.voting.web.servlet.json.JacksonObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation {@link AccessDeniedHandler} that sends {@link HttpServletResponse#SC_FORBIDDEN}
 * with  Json body {@link ErrorInfo} as a response.
 *
 * @author - A.Ovechnikov
 * @date - 16.01.2018
 */
@Component
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private MessageUtil messageUtil;

    /**
     * Handles an access denied failure.
     * Sends {@link HttpServletResponse#SC_FORBIDDEN}
     * with  Json body {@link ErrorInfo} as a response.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {//
        response.setContentType("application/hal+json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorInfo errorInfo = new ErrorInfo(request.getRequestURL(), ErrorType.ACCESS_DENIED,
                messageUtil.getMessage("error.accessDenied", null), exception.getMessage());
        String error = JacksonObjectMapper.getMapper().writeValueAsString(errorInfo);
        response.getWriter().write(error);
    }
}
