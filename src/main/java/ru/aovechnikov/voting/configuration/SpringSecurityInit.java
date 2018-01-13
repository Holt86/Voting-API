package ru.aovechnikov.voting.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;

/**
 * Registers the {@link DelegatingFilterProxy} to use the springSecurityFilterChain before
 * any other registered {@link Filter}.
 * SpringSecurityFilterChain is default mapped to “/*”.
 *
 * @author - A.Ovechnikov
 * @date - 13.01.2018
 */

public class SpringSecurityInit extends AbstractSecurityWebApplicationInitializer {

}
