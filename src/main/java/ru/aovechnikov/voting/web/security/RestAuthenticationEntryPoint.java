package ru.aovechnikov.voting.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
 * An {@link AuthenticationEntryPoint} that sends {@link HttpServletResponse#SC_UNAUTHORIZED}
 * with  Json body {@link ErrorInfo} as a response.
 *
 * @author - A.Ovechnikov
 * @date - 16.01.2018
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private MessageUtil messageUtil;

    /**
     * Sends {@link HttpServletResponse#SC_UNAUTHORIZED}
     * with  Json body {@link ErrorInfo} as a response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/hal+json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorInfo errorInfo = new ErrorInfo(request.getRequestURL(), ErrorType.NOT_AUTHENTICATION,
                messageUtil.getMessage("error.authentication", null), exception.getMessage());
        String error = JacksonObjectMapper.getMapper().writeValueAsString(errorInfo);
        response.getWriter().write(error);
    }
}