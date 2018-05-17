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

    public ManagedThread() {
        threadCounter++;
        this.id = threadCounter;
    }

    public ManagedThread managedRunnable(ManagedRunnable managedRunnable) {
        this.managedRunnable = managedRunnable;
        thread = new Thread(managedRunnable);
        return this;
    }

    public ManagedThread start() {
        ProcessManager.getInstance().addThread(this);
        thread.start();
        return this;
    }

    public Integer getId() {
        return id;
    }


    public Boolean getIsRunning() {
        return thread.isAlive();
    }


    public String getString() {
        return this.toString();
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
