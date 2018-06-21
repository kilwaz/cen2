package data.model.dao;

import data.SelectQuery;
import data.SelectResult;
import data.SelectResultRow;
import data.model.objects.Person;
import data.model.objects.json.JSONDataSource;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private static Logger log = Logger.getLogger(PersonDAO.class);

    public PersonDAO() {
    }

    @JSONDataSource("allPeople")
    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();

        SelectResult selectResult = (SelectResult) new SelectQuery("select uuid from person").execute();

        for (SelectResultRow resultRow: selectResult.getResults()) {
            String uuid = resultRow.getString("uuid");
            people.add(Person.load(DAO.UUIDFromString(uuid), Person.class));
        }

        return people;
    }
}
