package core.process;

import error.Error;
import org.apache.log4j.Logger;
import utils.managers.ProcessManager;

public class ManagedThread {
    public static Integer threadCounter = 0;
    private static Logger log = Logger.getLogger(ManagedThread.class);
    private Thread thread;
    private ManagedRunnable managedRunnable;
    private Integer id = -1;
    private String description = "";
    private String threadReference = null;

    public ManagedThread(ManagedRunnable managedRunnable, String description, String threadReference, Boolean startNow) {
        threadCounter++;
        this.id = threadCounter;
        this.description = description;
        this.managedRunnable = managedRunnable;
        this.threadReference = threadReference;

        thread = new Thread(managedRunnable);
        if (startNow) {
            start();
        }
    }

    public void start() {
        ProcessManager.getInstance().addThread(this);
        thread.start();
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsRunning() {
        return thread.isAlive();
    }


    public String getString() {
        return this.toString();
    }


    public String getThreadReference() {
        return threadReference;
    }

    public void setThreadReference(String threadReference) {
        this.threadReference = threadReference;
    }

    public void join() {
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Error.SDE_JOIN_THREAD.record().create(ex);
            }
        }
    }
}
