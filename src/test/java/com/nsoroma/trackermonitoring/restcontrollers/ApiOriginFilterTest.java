package com.nsoroma.trackermonitoring.restcontrollers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ApiOriginFilterTest {

    private ApiOriginFilter apiOriginFilter;
    private FilterConfig filterConfig;
    private MockHttpServletResponse mockHttpServletResponse;
    private ServletResponse servletResponse;
    private ServletRequest servletRequest;
    private MockFilterChain filterChain;

    @Before
    public void setUp() {
        apiOriginFilter = new ApiOriginFilter();
        filterConfig = mock(FilterConfig.class);
        servletRequest = mock(ServletRequest.class);
        servletResponse = mock(ServletResponse.class);
    }

    /*@Test
    public void testDoFilter() throws IOException, ServletException {
        when((HttpServletResponse) servletResponse).thenReturn(mockHttpServletResponse);
        apiOriginFilter.doFilter(servletRequest,servletResponse,filterChain);
        verify(filterChain).doFilter(servletRequest,servletResponse);
    }*/

    @Test
    public void destroy()
    {
        apiOriginFilter.destroy();
    }
    @Test
    public void init() throws ServletException{
        apiOriginFilter.init(filterConfig);
    }

}