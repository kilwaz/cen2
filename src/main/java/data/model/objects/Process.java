package data.model.objects;

import data.model.DatabaseObject;
import data.model.objects.json.JSONMappable;

import java.util.UUID;

public class Process extends DatabaseObject {
    private String command = "";
    private Boolean isCommandLine = Boolean.FALSE;

    public Process() {
        super();
    }

    public Process(UUID uuid, String command) {
        super(uuid);
        this.command = command;
    }

    @JSONMappable("command")
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
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
