package core.builders.requests;

import org.apache.log4j.Logger;
import requests.actions.ResetDatabase;
import requests.annotations.Action;
import requests.annotations.JSON;
import requests.annotations.JSP;
import requests.annotations.RequestName;
import requests.json.SourcesJSON;
import requests.pages.DatabaseAdmin;
import requests.pages.Hello;
import requests.pages.Import;

import java.util.HashMap;

public class RequestMapper {
    private static Logger log = Logger.getLogger(RequestMapper.class);

    private static HashMap<String, RequestMapping> mapping = new HashMap<>();

    public static void buildMappings() {
        //TODO Reflect files to get this list, should be possible!  Search request and json packages
        build(Hello.class);
        build(DatabaseAdmin.class);
        build(SourcesJSON.class);
        build(ResetDatabase.class);
        build(Import.class);
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
