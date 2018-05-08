package requests.json;

import core.Request;
import data.model.dao.SourceDAO;
import data.model.objects.json.JSONContainer;
import requests.annotations.JSON;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("sourcesJSON")
@JSON
public class SourcesJSON extends Request {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        SourceDAO sourceDAO = new SourceDAO();
        JSONContainer jsonContainer = new JSONContainer();
        jsonContainer.data(sourceDAO.getSources());
        jsonContainer.writeToResponse(response);
    }
}
