package core.process;

import org.apache.log4j.Logger;

import java.io.IOException;

public class ProcessHelper extends ManagedRunnable {
    private static Logger log = Logger.getLogger(ProcessHelper.class);

    private ManagedThread managedInputThread = null;
    private ManagedThread managedErrorThread = null;
    private ManagedThread managedOutputThread = null;

    private ProcessListener inputListener = null;
    private ProcessListener errorListener = null;
    private ProcessListener outputListener = null;

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

            log.info("Process running...");

            inputListener = new ProcessListener().inputStream(p.getInputStream());
            errorListener = new ProcessListener().inputStream(p.getErrorStream());
            outputListener = new ProcessListener().outputStream(p.getOutputStream());

            managedInputThread = new ManagedThread(inputListener, "", "", true);
            managedErrorThread = new ManagedThread(errorListener, "", "", true);
            managedOutputThread = new ManagedThread(outputListener, "", "", true);

            log.info("Waiting for process to finish");

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
        }
    }
}
