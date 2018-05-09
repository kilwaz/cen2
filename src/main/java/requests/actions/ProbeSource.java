package requests.actions;

import core.Request;
import data.model.dao.SourceDAO;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RequestName("probeSource")
@Action
public class ProbeSource extends Request {
    private static Logger log = Logger.getLogger(ProbeSource.class);

    public ProbeSource() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String jsonString = request.getParameter("json");

        if (jsonString != null) {
            JSONObject jsonObject = new JSONObject(jsonString);
            SourceDAO sourceDAO = new SourceDAO();
            Source source = sourceDAO.getSourceByUUID(jsonObject.getString("ref"));

            try {
                String s;
                StringBuilder jsonResponse = new StringBuilder();
                Process p = Runtime.getRuntime().exec("/usr/bin/ffprobe -v quiet -print_format json -show_format -show_streams " + source.getFileName());
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((s = br.readLine()) != null) {
                    jsonResponse.append(s);
                }

                p.waitFor();
                p.destroy();

                JSONContainer jsonContainer = new JSONContainer();
                jsonContainer.rawData(jsonResponse.toString());
                jsonContainer.writeToResponse(response);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
