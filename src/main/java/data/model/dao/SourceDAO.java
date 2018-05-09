package data.model.dao;


import data.SelectQuery;
import data.SelectResult;
import data.SelectResultRow;
import data.model.objects.Source;
import data.model.objects.json.JSONDataSource;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SourceDAO {
    private static Logger log = Logger.getLogger(SourceDAO.class);

    public SourceDAO() {
    }

    public Source getSourceByFileName(String fileName) {
        Source source = null;

        SelectResult selectResult = (SelectResult) new SelectQuery("select uuid from source where file_name = ?")
                .addParameter(fileName)
                .execute();

        for (SelectResultRow resultRow : selectResult.getResults()) {
            String uuid = resultRow.getString("uuid");
            source = Source.load(DAO.UUIDFromString(uuid), Source.class);
        }

        return source;
    }

    @JSONDataSource("allSources")
    public List<Source> getSources() {
        List<Source> sources = new ArrayList<>();

        SelectResult selectResult = (SelectResult) new SelectQuery("select uuid from source").execute();

        for (SelectResultRow resultRow : selectResult.getResults()) {
            String uuid = resultRow.getString("uuid");
            sources.add(Source.load(DAO.UUIDFromString(uuid), Source.class));
        }

        return sources;
    }

    public Source getSourceByUUID(String uuid) {
        return Source.load(DAO.UUIDFromString(uuid), Source.class);
    }
}
