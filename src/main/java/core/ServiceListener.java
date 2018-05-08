package core;

import core.builders.requests.RequestMapper;
import core.builders.resources.ResourceFileMapper;
import utils.AppManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServiceListener implements ServletContextListener {

    public ServiceListener() {

    }

    public void contextInitialized(ServletContextEvent event) {
        //ServletContext context = event.getServletContext();

        AppManager.init();
        RequestMapper.buildMappings();
        ResourceFileMapper.buildResources();

    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
