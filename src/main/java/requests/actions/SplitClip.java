package requests.actions;

import core.Request;
import core.process.Splitter;
import data.model.objects.Clip;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestName("splitClip")
@Action
public class SplitClip extends Request {
    private static Logger log = Logger.getLogger(SplitClip.class);

    public SplitClip() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid")) {
            Clip clip = Clip.load(UUID.fromString(jsonObject.getString("uuid")), Clip.class);

            Splitter splitter = new Splitter().clip(clip);
            splitter.execute();
        }
    }
}
