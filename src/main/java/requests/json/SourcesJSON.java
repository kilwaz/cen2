package requests.json;

import core.Request;
import data.model.dao.SourceDAO;
import data.model.objects.Source;
import org.json.JSONArray;
import org.json.JSONObject;
import requests.annotations.JSON;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequestName("sourcesJSON")
@JSON
public class SourcesJSON extends Request {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        SourceDAO sourceDAO = new SourceDAO();
        List<Source> sources = sourceDAO.getSources();

        JSONArray sourcesJSON = new JSONArray();

        for (Source source : sources) {
            JSONObject sourceJSON = new JSONObject();

            sourceJSON.put("filename", source.getFileName());
            sourceJSON.put("name", source.getName());

            sourcesJSON.put(sourceJSON);
        }


        //TODO Boiler code below
        try {
            PrintWriter out = response.getWriter();

            out.print(sourcesJSON);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
