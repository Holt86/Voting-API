package ru.aovechnikov.voting.util.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Util class for {@link MessageSource}.
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
@Component
public class MessageUtil {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code, Object[] arg){
        return messageSource.getMessage(code, arg, LocaleContextHolder.getLocale());
    }

    public String getMessage(MessageSourceResolvable resolvable) {
        return messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
    }
}
