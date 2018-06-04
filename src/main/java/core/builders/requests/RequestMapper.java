package core.builders.requests;

import core.Request;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import requests.annotations.Action;
import requests.annotations.JSON;
import requests.annotations.JSP;
import requests.annotations.RequestName;

import java.util.HashMap;
import java.util.Set;

public class RequestMapper {
    private static Logger log = Logger.getLogger(RequestMapper.class);

    private static HashMap<String, RequestMapping> mapping = new HashMap<>();

    public static void buildMappings() {
        Set<Class<? extends Request>> actions = new Reflections("requests.actions", new SubTypesScanner(false)).getSubTypesOf(Request.class);
        Set<Class<? extends Request>> pages = new Reflections("requests.pages", new SubTypesScanner(false)).getSubTypesOf(Request.class);
        Set<Class<? extends Request>> json = new Reflections("requests.json", new SubTypesScanner(false)).getSubTypesOf(Request.class);

        actions.forEach(RequestMapper::build);
        pages.forEach(RequestMapper::build);
        json.forEach(RequestMapper::build);
    }

    private static void build(Class requestClass) {
        RequestName requestNameAnnotation = (RequestName) requestClass.getAnnotation(RequestName.class);
        JSP jspAnnotation = (JSP) requestClass.getAnnotation(JSP.class);
        JSON jsonAnnotation = (JSON) requestClass.getAnnotation(JSON.class);
        Action actionAnnotation = (Action) requestClass.getAnnotation(Action.class);

        RequestMapping requestMapping = RequestMapping
                .mapping()
                .name(requestNameAnnotation.value())
                .requestClass(requestClass);

        if (jspAnnotation != null) { // JSP
            requestMapping.setRequestType(RequestMapping.REQUEST_TYPE_JSP)
                    .jspName(jspAnnotation.value());
        } else if (jsonAnnotation != null) { // JSON
            requestMapping.setRequestType(RequestMapping.REQUEST_TYPE_JSON);
        } else if (actionAnnotation != null) { // Action
            requestMapping.setRequestType(RequestMapping.REQUEST_TYPE_ACTION);
        }

        mapping.put(requestMapping.getName(), requestMapping);
    }

    public static RequestMapping getClassMapping(String requestName) {
        return mapping.get(requestName);
    }
}
