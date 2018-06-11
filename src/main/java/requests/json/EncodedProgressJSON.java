package requests.json;

import core.Request;
import data.model.objects.Clip;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.JSON;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestName("encodedProgressJSON")
@JSON
public class EncodedProgressJSON extends Request {
    private static Logger log = Logger.getLogger(EncodedProgressJSON.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);

        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid")) {
            String uuid = jsonObject.getString("uuid");
            if (!uuid.isEmpty()) {
                Clip clip = Clip.load(UUID.fromString(uuid), Clip.class);

                JSONContainer jsonContainer = new JSONContainer(clip.getEncodedProgress().asJSON());
                jsonContainer.writeToResponse(response);
            }
        }
    }
}