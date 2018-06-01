package data.model.dao;

import data.SelectQuery;
import data.SelectResult;
import data.SelectResultRow;
import data.model.objects.Mark;
import data.model.objects.Source;

import java.util.ArrayList;
import java.util.List;

public class MarkDAO {
    public MarkDAO() {
    }

    public List<Mark> getMarksFromSource(Source source) {
        List<Mark> marks = new ArrayList<>();

        SelectResult selectResult = (SelectResult) new SelectQuery("select uuid from mark where source = ?")
                .addParameter(source.getUuid())
                .execute();

        for (SelectResultRow resultRow : selectResult.getResults()) {
            String uuid = resultRow.getString("uuid");
            marks.add(Mark.load(DAO.UUIDFromString(uuid), Mark.class));
        }

        return marks;
    }
}
