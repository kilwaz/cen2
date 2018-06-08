package utils.timers;


import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.managers.ProcessManager;

public class ProcessClearJob implements Job {
    private static Logger log = Logger.getLogger(ProcessClearJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        ProcessManager.getInstance().clearFinishedProcesses();
    }
}
