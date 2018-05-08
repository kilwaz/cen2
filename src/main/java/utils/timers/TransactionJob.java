package utils.timers;


import utils.managers.DatabaseTransactionManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TransactionJob implements Job {
    private static Logger log = Logger.getLogger(TransactionJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DatabaseTransactionManager.getInstance().checkIfNeedToFinalise();
    }
}
