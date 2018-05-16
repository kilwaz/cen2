package core.process;

import org.apache.log4j.Logger;

import java.io.*;

public class ProcessListener extends ManagedRunnable {
    private static Logger log = Logger.getLogger(ProcessListener.class);
    private BufferedReader reader;

    public ProcessListener() {
        super();
        super.setCommandLine(false);
    }

    public ProcessListener inputStream(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
        return this;
    }

    public ProcessListener outputStream(OutputStream outputStream) {
        return this;
    }

    private void listen() {
        if (reader == null) {
            return;
        }
        log.info("Starting listener...");
        try {
            String line = "";
            while ((line = reader.readLine()) != null) {
                log.info(line);
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
}
