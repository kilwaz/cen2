package utils.managers;

import core.process.ManagedThread;
import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import utils.timers.ProcessClearJob;

import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
    private static Logger log = Logger.getLogger(ProcessManager.class);

    private static ProcessManager instance;
    private List<ManagedThread> runningThreads;

    public ProcessManager() {
        instance = this;
        runningThreads = new ArrayList<>();

        JobDetail processClearJob = JobBuilder.newJob(ProcessClearJob.class).build();

        SimpleScheduleBuilder processClearSimpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        TriggerBuilder processClearTriggerBuilder = TriggerBuilder.newTrigger();

        processClearSimpleScheduleBuilder.repeatForever().withIntervalInMilliseconds(30000); // 30 Seconds

        JobManager.getInstance().scheduleJob(processClearJob, processClearTriggerBuilder.withSchedule(processClearSimpleScheduleBuilder).build());
        processClearTriggerBuilder.startNow();
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
        return runningThreads.size();
    }

    public List<ManagedThread> getRunningThreads() {
        return runningThreads;
    }

    public void clearFinishedProcesses() {
        List<ManagedThread> loopList = new ArrayList<>(runningThreads);

        for (ManagedThread managedThread : loopList) {
            if (!managedThread.getIsRunning()) {
                runningThreads.remove(managedThread);
            }
        }
    }
}
