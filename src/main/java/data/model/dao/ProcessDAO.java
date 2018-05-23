package data.model.dao;


import data.SelectQuery;
import data.SelectResult;
import data.SelectResultRow;
import data.model.objects.Process;
import data.model.objects.json.JSONDataSource;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ProcessDAO {
    private static Logger log = Logger.getLogger(ProcessDAO.class);

    public ProcessDAO() {
    }

    @JSONDataSource("allProcesses")
    public List<Process> getProcesses() {
        return retrieveProcessList(new SelectQuery("select uuid from process"));
    }

    @JSONDataSource("allCommandLineProcesses")
    public List<Process> getCommandLineProcesses() {
        return retrieveProcessList((SelectQuery) new SelectQuery("select uuid from process where is_command_line = ?").addParameter(Boolean.TRUE));
    }

    private List<Process> retrieveProcessList(SelectQuery selectQuery) {
        List<Process> processes = new ArrayList<>();

        SelectResult selectResult = (SelectResult) selectQuery.execute();

        for (SelectResultRow resultRow : selectResult.getResults()) {
            String uuid = resultRow.getString("uuid");
            processes.add(Process.load(DAO.UUIDFromString(uuid), Process.class));
        }

        return processes;
    }

    public Process getProcessByUUID(String uuid) {
        return Process.load(DAO.UUIDFromString(uuid), Process.class);
    }
}
