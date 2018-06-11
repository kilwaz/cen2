package data.model.dao;

import data.SelectQuery;
import data.SelectResult;
import data.SelectResultRow;
import data.model.objects.Clip;
import data.model.objects.Source;
import data.model.objects.json.JSONDataSource;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClipDAO {
    private static Logger log = Logger.getLogger(ClipDAO.class);

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

    @JSONDataSource("allEncodingClips")
    public List<Clip> allEncodingClips() {
        List<Clip> clips = new ArrayList<>();

        SelectResult selectResult = (SelectResult) new SelectQuery("select uuid from clip where locked_in = ?")
                .addParameter(Boolean.TRUE)
                .execute();

        for (SelectResultRow resultRow : selectResult.getResults()) {
            String uuid = resultRow.getString("uuid");
            clips.add(Clip.load(DAO.UUIDFromString(uuid), Clip.class));
        }

        return clips;
    }
}
