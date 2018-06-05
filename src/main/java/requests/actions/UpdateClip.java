package requests.actions;

import core.Request;
import data.model.objects.Clip;
import data.model.objects.Mark;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestName("updateClip")
@Action
public class UpdateClip extends Request {
    private static Logger log = Logger.getLogger(UpdateClip.class);

    public UpdateClip() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("clipUuid") && jsonObject.has("startMarkUuid")) {
            String clipUuid = jsonObject.getString("clipUuid");
            String startMarkUuid = jsonObject.getString("startMarkUuid");
            if (!clipUuid.isEmpty() && !startMarkUuid.isEmpty()) {
                Clip clip = Clip.load(UUID.fromString(clipUuid), Clip.class);
                Mark mark = Mark.load(UUID.fromString(startMarkUuid), Mark.class);
                clip.setStartMark(mark);
                clip.save();
            }
        } else if (jsonObject.has("clipUuid") && jsonObject.has("endMarkUuid")) {
            String clipUuid = jsonObject.getString("clipUuid");
            String endMarkUuid = jsonObject.getString("endMarkUuid");
            if (!clipUuid.isEmpty() && !endMarkUuid.isEmpty()) {
                Clip clip = Clip.load(UUID.fromString(clipUuid), Clip.class);
                Mark mark = Mark.load(UUID.fromString(endMarkUuid), Mark.class);
                clip.setEndMark(mark);
                clip.save();
            }
        }
    }
}
