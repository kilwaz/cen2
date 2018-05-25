package requests.actions;

import core.Request;
import core.process.Splitter;
import data.model.dao.SourceDAO;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("splitSource")
@Action
public class SplitSource extends Request {
    private static Logger log = Logger.getLogger(SplitSource.class);

    public SplitSource() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("uuid") && jsonObject.has("startTime") && jsonObject.has("endTime")) {
            SourceDAO sourceDAO = new SourceDAO();
            Source source = sourceDAO.getSourceByUUID(jsonObject.getString("uuid"));

            Double startTime = jsonObject.getDouble("startTime");
            Double endTime = jsonObject.getDouble("endTime");

            Splitter splitter = new Splitter()
                    .source(source)
                    .startTime(startTime)
                    .endTime(endTime);
            splitter.execute();
        }
    }
}
