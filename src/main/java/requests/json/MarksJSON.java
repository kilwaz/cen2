package requests.json;

import core.Request;
import data.model.dao.MarkDAO;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.JSON;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestName("marksJSON")
@JSON
public class MarksJSON extends Request {
    private static Logger log = Logger.getLogger(MarksJSON.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);

        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid")) {
            String uuid = jsonObject.getString("uuid");
            if (!uuid.isEmpty()) {
                Source source = Source.load(UUID.fromString(uuid), Source.class);

                MarkDAO markDAO = new MarkDAO();
                JSONContainer jsonContainer = new JSONContainer();
                jsonContainer.dbData(markDAO.getMarksFromSource(source));
                jsonContainer.writeToResponse(response);
            }
        }
    }
}
