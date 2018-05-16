package utils.managers;

import core.process.ManagedThread;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
    private static ProcessManager instance;
    private List<ManagedThread> runningThreads;
    private Integer activeThreads = 0;

    private static Logger log = Logger.getLogger(ProcessManager.class);

    public ProcessManager() {
        instance = this;
        runningThreads = new ArrayList<>();
    }

    public synchronized static ProcessManager getInstance() {
        if (instance == null) {
            instance = new ProcessManager();
        }

        return instance;
    }

    // Synchronized method as we are accessing runningThreads list
    public synchronized void addThread(ManagedThread thread) {
        runningThreads.add(thread);
    }

    public synchronized Integer getActiveThreads() {
        return activeThreads;
    }

    public List<ManagedThread> getRunningThreads() {
        return runningThreads;
    }
}
