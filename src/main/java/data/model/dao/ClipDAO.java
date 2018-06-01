package data.model.dao;

import data.SelectQuery;
import data.SelectResult;
import data.SelectResultRow;
import data.model.objects.Clip;
import data.model.objects.Source;

import java.util.ArrayList;
import java.util.List;

public class ClipDAO {

    public ClipDAO() {
    }

    public List<Clip> getClipsFromSource(Source source) {
        List<Clip> clips = new ArrayList<>();

        SelectResult selectResult = (SelectResult) new SelectQuery("select uuid from clip where source = ?")
                .addParameter(source.getUuid())
                .execute();

        for (SelectResultRow resultRow : selectResult.getResults()) {
            String uuid = resultRow.getString("uuid");
            clips.add(Clip.load(DAO.UUIDFromString(uuid), Clip.class));
        }

        return clips;
    }
}
