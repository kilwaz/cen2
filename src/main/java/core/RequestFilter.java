package core;

import core.builders.requests.RequestMapper;
import core.builders.requests.RequestMapping;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RequestFilter implements Filter {
    private static Logger log = Logger.getLogger(RequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        if (requestURI.contains("/")) { // URL is not as expected
            String page = requestURI.substring(requestURI.lastIndexOf("/") + 1);

            RequestMapping requestMapping = RequestMapper.getClassMapping(page);
            if (requestMapping == null) {
                log.info("Page '" + page + "' cannot be found");
            } else {
                Constructor<?> ctor;
                try {
                    ctor = requestMapping.getRequestClass().getConstructor();
                    Request requestClass = (Request) ctor.newInstance();

                    if("GET".equals(request.getMethod())){
                        requestClass.doGet(request, response);
                    } else if("POST".equals(request.getMethod())){
                        requestClass.doPost(request, response);
                    }

                    if (requestMapping.getRequestType().equals(RequestMapping.REQUEST_TYPE_JSP)) {
                        RequestDispatcher requestDispatcher = servletRequest.getRequestDispatcher("/WEB-INF/pages/" + requestMapping.getJspName());
                        requestDispatcher.forward(servletRequest, servletResponse);
                    } else if (requestMapping.getRequestType().equals(RequestMapping.REQUEST_TYPE_JSON)) {
                        response.setContentType("application/json");
                        log.info("Setting the response type as json");
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void destroy() {
    }
}
