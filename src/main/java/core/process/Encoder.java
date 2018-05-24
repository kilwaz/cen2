package core.process;

import data.model.objects.Source;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Encoder implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Encoder.class);
    private Source source;
    private ManagedThread managedThread;
    private ProcessHelper processHelper;

    private Integer pass = 0;

    private List<Flow.Subscription> subscriptions = new ArrayList<>();

    public Encoder() {
        super();
    }

    public Encoder source(Source source) {
        this.source = source;
        return this;
    }

    public Encoder pass(Integer pass) {
        this.pass = pass;
        return this;
    }

    public void execute() {
        var encodedProgress = source.getEncodedProgress();
        encodedProgress.setPassPhase(pass);
        encodedProgress.save();
        source.setEncodedProgress(encodedProgress);
        source.save();

        String command = "";
        if (pass.equals(1)) {
            command = "/usr/bin/ffmpeg -y -i " + source.getFileName() + " -c:v libvpx-vp9 -pass " + pass + " -passlogfile /home/kilwaz/srcLog/" + source.getUuidString() + " -b:v 1000K -threads 8 -speed 4 -tile-columns 6 -frame-parallel 1 -an -f webm /dev/null";
        } else if (pass.equals(2)) {
            command = "/usr/bin/ffmpeg -y -i " + source.getFileName() + " -c:v libvpx-vp9 -pass " + pass + " -passlogfile /home/kilwaz/srcLog/" + source.getUuidString() + " -b:v 1000K -threads 8 -speed 4 -tile-columns 6 -frame-parallel 1 -an -f webm /home/kilwaz/srcDone/encoded-" + source.getUuidString();
        }

        processHelper = new ProcessHelper()
                .command(command)
                .processDescription("Encoding " + source.getFileName())
                .processReference(source.getUuidString())
                .logSubscriber(this);

        managedThread = new ManagedThread()
                .managedRunnable(processHelper)
                .start();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(1);
        subscriptions.add(subscription);
    }

    @Override
    public void onNext(LogMessage item) {
        log.info(item.getMessage());
        var message = item.getMessage();
        if (message.startsWith("frame=")) {
            message = message.substring(6).trim();
            message = message.substring(0, message.indexOf(" "));

            Integer frame = Integer.parseInt(message);
            var encodedProgress = source.getEncodedProgress();
            if (pass.equals(1)) {
                encodedProgress.setPass1Progress(frame);
            } else if (pass.equals(2)) {
                encodedProgress.setPass2Progress(frame);

                File pass1LogFile = new File("/home/kilwaz/srcLog/" + source.getUuidString() + "-0.log");
                if (pass1LogFile.exists()) {
                    pass1LogFile.delete();
                }
            }
            encodedProgress.save();
        }

        for (Flow.Subscription subscription : subscriptions) {
            subscription.request(1);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("We had an error from a subscription!");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        log.info("We completed a subscription!");
    }
}
