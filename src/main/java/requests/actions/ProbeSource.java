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
            JSONContainer jsonContainerRequest = new JSONContainer(jsonString);

            JSONObject jsonObject = jsonContainerRequest.toJSONObject();
            SourceDAO sourceDAO = new SourceDAO();
            Source source = sourceDAO.getSourceByUUID(jsonObject.getString("ref"));

            JSONContainer jsonContainer = new JSONContainer(source.getSourceInfo());
            jsonContainer.writeToResponse(response);
        }
    }
}
