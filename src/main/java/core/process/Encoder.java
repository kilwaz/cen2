package core.process;

import data.model.objects.Source;
import org.apache.log4j.Logger;

public class Encoder {
    private static Logger log = Logger.getLogger(Encoder.class);
    private Source source;

    public Encoder() {
        super();
    }

    public Encoder source(Source source) {
        this.source = source;
        return this;
    }

    public void execute() {
        String command = "/usr/bin/ffmpeg -y -i " + source.getFileName() + " -c:v libvpx-vp9 -pass 1 -passlogfile " + source.getUuidString() + " -b:v 1000K -threads 8 -speed 4 -tile-columns 6 -frame-parallel 1 -an -f webm /dev/null";

        ProcessHelper processHelper = new ProcessHelper().command(command);
        ManagedThread managedThread = new ManagedThread(processHelper, "Encoding " + source.getFileName(), source.getUuidString(), true);
    }
}
