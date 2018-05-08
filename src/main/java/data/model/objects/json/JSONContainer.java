package data.model.objects.json;

import org.json.JSONArray;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JSONContainer<DatabaseObject> {
    private List<DatabaseObject> data = new ArrayList<>();

    public JSONContainer() {

    }

    public JSONContainer data(List<DatabaseObject> data) {
        this.data = data;
        return this;
    }

    public JSONArray getData() {
        return JSONMapper.build().process(data);
    }

    public void writeToResponse(HttpServletResponse response) {
        //TODO Boiler code below
        try {
            PrintWriter out = response.getWriter();

            out.print(getData());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String writeToString() {
        return JSONMapper.build().process(data).toString();
    }
}
