package requests.actions;

import core.Request;
import data.model.objects.Clip;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestName("createClip")
@Action
public class CreateClip extends Request {
    private static Logger log = Logger.getLogger(CreateClip.class);

    public CreateClip() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid")) {
            String uuid = jsonObject.getString("uuid");
            if (!uuid.isEmpty()) {
                Source source = Source.load(UUID.fromString(uuid), Source.class);

                Clip clip = Clip.create(Clip.class);
                clip.setSource(source);
                clip.save();
            }
        }
    }
}
