package requests.json;

import core.Request;
import data.model.dao.MarkDAO;
import data.model.dao.SourceDAO;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.JSON;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("marksJSON")
@JSON
public class MarksJSON extends Request {
    private static Logger log = Logger.getLogger(SourceInfoJSON.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);

        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid")) {
            SourceDAO sourceDAO = new SourceDAO();
            Source source = sourceDAO.getSourceByUUID(jsonObject.getString("uuid"));

            MarkDAO markDAO = new MarkDAO();
            JSONContainer jsonContainer = new JSONContainer();
            jsonContainer.dbData(markDAO.getMarksFromSource(source));
            jsonContainer.writeToResponse(response);
        } else {
            log.info("source ref not provided");
        }
    }
}
