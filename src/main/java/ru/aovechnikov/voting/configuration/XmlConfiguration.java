package ru.aovechnikov.voting.configuration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

/**
 *  In Servlet 3.0+ environments, this class replaces the traditional {@code web.xml}-based approach in order to configure the
 * {@link ServletContext} programmatically.
 * <p/>
 * Create the Spring "<strong>root</strong>" application context.<br/>
 * Register a {@link DispatcherServlet}  in the servlet context.<br/>
 * For both servlets, register a {@link CharacterEncodingFilter}.
 * <p/>
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */

public class XmlConfiguration extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfiguration.class, DbConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebMvcConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return new Filter[]{encodingFilter};
    }


}
