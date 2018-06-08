package requests.actions;

import core.Request;
import data.model.objects.Clip;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestName("unlockClip")
@Action
public class UnlockClip extends Request {
    private static Logger log = Logger.getLogger(UnlockClip.class);

    public UnlockClip() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid")) {
            Clip clip = Clip.load(UUID.fromString(jsonObject.getString("uuid")), Clip.class);
            if (clip != null) {
                clip.setLockedIn(false);
                clip.save();
            }
        }
    }
}
