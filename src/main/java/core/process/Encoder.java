package core.process;

import data.model.objects.EncodedProgress;
import data.model.objects.Source;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Encoder implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Encoder.class);
    private Source source;
    private EncodedProgress encodedProgress;
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
        encodedProgress = EncodedProgress.create(EncodedProgress.class);
        encodedProgress.setPassPhase(1);
        encodedProgress.save();
        source.setEncodedProgress(encodedProgress);
        source.save();

        String command = "";
        if (pass.equals(1)) {
            command = "/usr/bin/ffmpeg -y -i " + source.getFileName() + " -c:v libvpx-vp9 -pass " + pass + " -passlogfile /home/kilwaz/" + source.getUuidString() + " -b:v 1000K -threads 8 -speed 4 -tile-columns 6 -frame-parallel 1 -an -f webm /dev/null";
        } else {
            command = "/usr/bin/ffmpeg -y -i " + source.getFileName() + " -c:v libvpx-vp9 -pass " + pass + " -passlogfile /home/kilwaz/" + source.getUuidString() + " -b:v 1000K -threads 8 -speed 4 -tile-columns 6 -frame-parallel 1 -an -f webm /home/kilwaz/" + source.getUuidString();
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
        log.info(item.getProcessorType() + " - " + item.getMessage());
        encodedProgress.setPass1Progress(2);
        encodedProgress.save();
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
