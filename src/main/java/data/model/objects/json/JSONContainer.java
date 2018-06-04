package data.model.objects.json;

import error.Error;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JSONContainer<DatabaseObject> {
    private static Logger log = Logger.getLogger(JSONContainer.class);

    private List<DatabaseObject> data = new ArrayList<>();
    private String rawData = null;

    public JSONContainer() {

    }

    public JSONContainer(String rawData) {
        this.rawData = rawData;
    }

    public JSONContainer(JSONObject jsonObject) {
        this.rawData = jsonObject.toString();
    }

    public JSONContainer dbDataList(List<DatabaseObject> dataList) {
        this.data = dataList;
        this.rawData = null;
        return this;
    }

    public JSONContainer dbDataItem(DatabaseObject dataItem) {
        this.data = new ArrayList<>();
        this.data.add(dataItem);
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
                out.print(toJSONObject());
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

    public JSONObject toJSONObject() {
        try {
            return new JSONObject(rawData);
        } catch (JSONException ex) {
            try {
                if (rawData != null && !rawData.isEmpty()) {
                    new JSONObject(rawData);
                } else {
                    new JSONObject();
                }
            } catch (JSONException ex1) {
                Error.NOT_VALID_JSON.record().additionalInformation("JSON - " + rawData).create(ex1);
            }
        }
        return new JSONObject();
    }
}
