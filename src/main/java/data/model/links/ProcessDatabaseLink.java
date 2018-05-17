package data.model.links;


import data.model.DatabaseLink;
import data.model.objects.Process;

import java.util.UUID;

public class ProcessDatabaseLink extends DatabaseLink {
    public ProcessDatabaseLink() {
        super("process", Process.class);

        // Make sure the order is the same as column order in database
        link("uuid", method("getUuidString"), method("setUuid", UUID.class)); // 1
        link("command", method("getCommand"), method("setCommand", String.class)); // 2
        link("is_command_line", method("isCommandLine"), method("setIsCommandLine", Boolean.class)); // 3
    }
}
