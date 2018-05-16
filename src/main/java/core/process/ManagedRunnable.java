package core.process;

import data.model.objects.Process;

public class ManagedRunnable implements Runnable {

    private Process process;

    public ManagedRunnable() {
        process = Process.create(Process.class);
        process.save();
    }

    @Override
    public void run() {

    }

    public String getCommand() {
        return process.getCommand();
    }

    public void setCommand(String command) {
        process.setCommand(command);
        process.save();

    }

    public Boolean getCommandLine() {
        return process.isCommandLine();
    }

    public void setCommandLine(Boolean isCommandLine) {
        process.setIsCommandLine(isCommandLine);
        process.save();
    }
}
