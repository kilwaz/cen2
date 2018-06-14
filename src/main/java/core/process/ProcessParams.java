package core.process;

import java.util.ArrayList;
import java.util.List;

public class ProcessParams {
    private List<String> params = new ArrayList<>();
    private String path = "";

    public ProcessParams() {

    }

    public ProcessParams path(String path) {
        this.path = path;
        return this;
    }

    public ProcessParams add(String parameter) {
        params.add(parameter);
        return this;
    }

    public List<String> getParams() {
        return params;
    }

    public String getPath() {
        return path;
    }

    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            sb.append(param).append(" ");
        }
        return path + " " + sb.toString();
    }

    public List<String> getParamsAsList() {
        List<String> paramList = new ArrayList<>();
        paramList.add(path);
        paramList.addAll(params);
        return paramList;
    }
}
