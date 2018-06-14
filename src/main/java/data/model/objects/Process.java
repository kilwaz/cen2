package data.model.objects;

import core.process.ProcessParams;
import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;

import java.util.UUID;

public class Process extends DatabaseObject {
    private ProcessParams command;
    private Boolean isCommandLine = Boolean.FALSE;

    public Process() {
        super();
    }

    public Process(UUID uuid, ProcessParams command) {
        super(uuid);
        this.command = command;
    }

    public ProcessParams getCommand() {
        return command;
    }

    public void setCommand(ProcessParams command) {
        this.command = command;
    }

    @JSONMappable("isCommandLine")
    public Boolean isCommandLine() {
        return isCommandLine;
    }

    public void setIsCommandLine(Boolean isCommandLine) {
        this.isCommandLine = isCommandLine;
    }
}
