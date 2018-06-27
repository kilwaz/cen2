package requests.actions;

import core.Request;
import data.model.objects.Person;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import requests.annotations.Action;
import requests.annotations.RequestName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestName("createPerson")
@Action
public class CreatePerson extends Request {
    private static Logger log = Logger.getLogger(CreatePerson.class);

    public CreatePerson() {
        super();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        super.doPost(request, response);
        JSONContainer incomingRequestData = getIncomingRequestData();
        JSONObject jsonObject = incomingRequestData.toJSONObject();

        if (jsonObject.has("firstName") && jsonObject.has("lastName")) {
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("lastName");

            Person person = Person.create(Person.class);
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.save();

            JSONContainer jsonContainer = new JSONContainer();
            jsonContainer.dbDataItem(person);
            jsonContainer.writeToResponse(response);
        }
    }
}
