package core.process;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ProcessListener extends ManagedRunnable {
    private static Logger log = Logger.getLogger(ProcessListener.class);

    private static final Integer PROCESSOR_UNSET = -1;
    private static final Integer PROCESSOR_INPUT = 1;
    private static final Integer PROCESSOR_ERROR = 2;
    private static final Integer PROCESSOR_OUTPUT = 3;

    private Integer processorType = PROCESSOR_UNSET;
    private ProcessHelper processHelper;
    private BufferedReader reader;
    private SubmissionPublisher<LogMessage> logPublisher = new SubmissionPublisher<>();

    public ProcessListener() {
        super();
        super.setCommandLine(false);
    }

    public ProcessListener inputStream(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
        processorType = PROCESSOR_INPUT;
        return this;
    }

    public ProcessListener processHelper(ProcessHelper processHelper) {
        this.processHelper = processHelper;
        processorType = PROCESSOR_INPUT;
        return this;
    }

    public ProcessListener errorStream(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
        processorType = PROCESSOR_ERROR;
        return this;
    }

    public ProcessListener outputStream(OutputStream outputStream) {
        return this;
    }

    private void listen() {
        if (reader == null) {
            return;
        }
        try {
            var line = "";
            while ((line = reader.readLine()) != null) {
                var logMessage = new LogMessage()
                        .message(line)
                        .processorType(processorType)
                        .processHelper(processHelper);
                logPublisher.submit(logMessage);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        listen();
    }

    public void subscribe(Flow.Subscriber<LogMessage> subscriber) {
        logPublisher.subscribe(subscriber);
    }
}
