package requests.actions;

import core.Request;
import core.process.Encoder;
import data.model.dao.SourceDAO;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("encodeSource")
@Action
public class EncodeSource extends Request {
    private static Logger log = Logger.getLogger(EncodeSource.class);

    public EncodeSource() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("ref")) {
            SourceDAO sourceDAO = new SourceDAO();
            Source source = sourceDAO.getSourceByUUID(jsonObject.getString("ref"));

            Encoder encoder = new Encoder().source(source);
            encoder.execute();
        }
    }
}
