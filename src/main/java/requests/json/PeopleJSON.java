package requests.json;

import core.Request;
import data.model.dao.PersonDAO;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import requests.annotations.JSON;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("peopleJSON")
@JSON
public class PeopleJSON extends Request {
    private static Logger log = Logger.getLogger(PeopleJSON.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        super.doGet(request, response);
        PersonDAO personDAO = new PersonDAO();

        JSONContainer jsonContainer = new JSONContainer();
        jsonContainer.dbDataList(personDAO.getPeople());
        jsonContainer.writeToResponseAsDataTable(response);
    }
}
