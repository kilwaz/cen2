package core;

import data.model.objects.json.JSONContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Request {
    private JSONContainer incomingRequestData = new JSONContainer();

    public Request() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        parseIncomingRequestData(request);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        parseIncomingRequestData(request);
    }

    private void parseIncomingRequestData(HttpServletRequest request) {
        String jsonString = request.getParameter("json");

        if (jsonString != null) {
            incomingRequestData.rawData(jsonString);
        }
    }

    public JSONContainer getIncomingRequestData() {
        return incomingRequestData;
    }
}
