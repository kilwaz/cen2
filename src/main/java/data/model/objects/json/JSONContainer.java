package data.model.objects.json;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JSONContainer<DatabaseObject> {
    private List<DatabaseObject> data = new ArrayList<>();
    private String rawData = null;

    public JSONContainer() {

    }

    public JSONContainer dbData(List<DatabaseObject> data) {
        this.data = data;
        this.rawData = null;
        return this;
    }

    public JSONArray getData() {
        return JSONMapper.build().process(data);
    }

    public JSONContainer rawData(String rawData) {
        this.rawData = rawData;
        this.data = null;
        return this;
    }

    public void writeToResponse(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();

            if (rawData != null) {
                JSONObject jsonObject = new JSONObject(rawData);
                out.print(jsonObject);
            } else {
                out.print(getData());
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String writeToString() {
        return JSONMapper.build().process(data).toString();
    }
}
