package core.process;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.Flow;

public class ProcessHelper extends ManagedRunnable {
    private static Logger log = Logger.getLogger(ProcessHelper.class);

    private ManagedThread managedInputThread = null;
    private ManagedThread managedErrorThread = null;
    private ManagedThread managedOutputThread = null;

    private ProcessListener inputListener = null;
    private ProcessListener errorListener = null;
    private ProcessListener outputListener = null;

    private String processReference = null;
    private String processDescription = null;

    private Flow.Subscriber<LogMessage> logSubscriber;

    private Process p = null;

    public ProcessHelper() {
        super();
        super.setCommandLine(true);
    }

    public ProcessHelper command(String command) {
        super.setCommand(command);
        return this;
    }

    public void run() {
        p = null;

        inputListener = null;
        errorListener = null;
        outputListener = null;
        try {
            p = Runtime.getRuntime().exec(getCommand());

            inputListener = new ProcessListener().inputStream(p.getInputStream()).processHelper(this);
//            errorListener = new ProcessListener().errorStream(p.getErrorStream()).processHelper(this);
//            outputListener = new ProcessListener().outputStream(p.getOutputStream()).processHelper(this);

            managedInputThread = new ManagedThread().managedRunnable(inputListener).start();
//            managedErrorThread = new ManagedThread().managedRunnable(errorListener).start();
//            managedOutputThread = new ManagedThread().managedRunnable(outputListener).start();

            triggerLopSubscribe();

            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }

            if (inputListener != null) {
                inputListener.close();
            }
            if (errorListener != null) {
                errorListener.close();
            }
            if (outputListener != null) {
                outputListener.close();
            }

            completeProcess();
        }
    }

    private void triggerLopSubscribe() {
        if (logSubscriber != null) {
            inputListener.subscribe(logSubscriber);
//            errorListener.subscribe(logSubscriber);
//            outputListener.subscribe(logSubscriber);
        }
    }

    public ProcessHelper logSubscriber(Flow.Subscriber<LogMessage> logSubscriber) {
        this.logSubscriber = logSubscriber;
        return this;
    }

    public ProcessHelper processReference(String processReference) {
        this.processReference = processReference;
        return this;
    }

    public ProcessHelper processDescription(String processDescription) {
        this.processDescription = processDescription;
        return this;
    }

    public void unsubscribeAll() {
        if (logSubscriber != null) {
            inputListener.close();
        }
    }

    public String getProcessReference() {
        return processReference;
    }

    public String getProcessDescription() {
        return processDescription;
    }
}
