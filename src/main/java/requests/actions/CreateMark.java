package requests.actions;

import core.Request;
import data.model.dao.SourceDAO;
import data.model.objects.Mark;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("createMark")
@Action
public class CreateMark extends Request {
    private static Logger log = Logger.getLogger(CreateMark.class);

    public CreateMark() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid") && jsonObject.has("time")) {
            SourceDAO sourceDAO = new SourceDAO();
            Source source = sourceDAO.getSourceByUUID(jsonObject.getString("uuid"));

            Double time = jsonObject.getDouble("time");

            Mark mark = Mark.create(Mark.class);
            mark.setSource(source);
            mark.setTime(time);
            mark.save();
        }
    }
}
