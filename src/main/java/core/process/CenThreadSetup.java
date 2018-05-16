package core.process;

import org.apache.log4j.Logger;

public class CenThreadSetup implements Runnable {
    private ManagedThread managedThread;
    private static Logger log = Logger.getLogger(CenThreadSetup.class);

    public CenThreadSetup(ManagedThread managedThread) {
        this.managedThread = managedThread;
    }

    @Override
    public void run() {
        ManagedThread.threadCounter++;
//        ProcessManager.getInstance().addThread(managedThread);
        if (managedThread.getThreadReference() != null) {
//            ProcessManager.getInstance().addThreadToCollection(managedThread.getThreadReference(), managedThread);
        }
    }
}
