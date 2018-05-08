package core.builders.requests;

public class RequestMapping {
    public static final Integer REQUEST_TYPE_JSP = 1;
    public static final Integer REQUEST_TYPE_JSON = 2;
    public static final Integer REQUEST_TYPE_ACTION = 3;

    private Integer requestType;
    private String jspName;
    private Class requestClass;
    private String name;

    private RequestMapping() {
    }

    public static RequestMapping mapping() {
        return new RequestMapping();
    }

    public RequestMapping name(String name) {
        this.name = name;
        return this;
    }

    public RequestMapping jspName(String jspName) {
        this.jspName = jspName;
        return this;
    }

    public RequestMapping requestClass(Class requestClass) {
        this.requestClass = requestClass;
        return this;
    }

    public String getJspName() {
        return jspName;
    }

    public Class getRequestClass() {
        return requestClass;
    }

    public String getName() {
        return name;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public RequestMapping setRequestType(Integer requestType) {
        this.requestType = requestType;
        return this;
    }
}
