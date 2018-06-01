package requests.actions;

import core.Request;
import data.model.objects.Mark;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestName("removeMark")
@Action
public class RemoveMark extends Request {
    private static Logger log = Logger.getLogger(RemoveMark.class);

    public RemoveMark() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid")) {
            Mark mark = Mark.load(UUID.fromString(jsonObject.getString("uuid")), Mark.class);
            if (mark != null) {
                mark.delete();
                log.info("Delete mark " + jsonObject.getString("uuid"));
            } else {
                log.info("Could find mark to delete " + jsonObject.getString("uuid"));
            }
        }
    }
}
