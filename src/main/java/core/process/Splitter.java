package core.process;

import data.model.objects.Clip;
import data.model.objects.Source;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Splitter implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Splitter.class);
    private Source source;
    private ManagedThread managedThread;
    private ProcessHelper processHelper;
    private StringBuilder jsonResult = new StringBuilder();

    private Double startTime = 0d;
    private Double endTime = 0d;

    private List<Flow.Subscription> subscriptions = new ArrayList<>();

    public Splitter() {
        super();
    }

    public Splitter source(Source source) {
        this.source = source;
        return this;
    }

    public Splitter startTime(Double startTime) {
        this.startTime = startTime;
        return this;
    }

    public Splitter endTime(Double endTime) {
        this.endTime = endTime;
        return this;
    }

    private String convertTimeToString(Double timeInSeconds) {
        Double timeHour = Math.floor(timeInSeconds / 3600);
        Double timeMin = Math.floor((timeInSeconds / 60) % 60);
        Double timeSec = Math.floor(timeInSeconds % 60);

        return String.format("%02d", timeHour.intValue()) + ":" + String.format("%02d", timeMin.intValue()) + ":" + String.format("%02d", timeSec.intValue());
    }

    public void execute() {
        Clip clip = Clip.create(Clip.class);
        clip.setSource(source);
        clip.setStartTime(startTime);
        clip.setEndTime(endTime);
        clip.setFileName("/home/kilwaz/srcDone/clip-" + clip.getUuidString() + "." + source.getFileExtension());
        clip.save();

        String command = "/usr/bin/ffmpeg -ss " + convertTimeToString(startTime) + " -i " + source.getFileName() + " -to " + convertTimeToString(endTime - startTime) + " -acodec copy -vcodec copy -async 1 -y " + clip.getFileName();

        log.info("Splitter command = " + command);

        processHelper = new ProcessHelper()
                .command(command)
                .processDescription("Splitter " + clip.getFileName())
                .processReference(clip.getUuidString())
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
        if (ProcessListener.PROCESSOR_INPUT.equals(item.getProcessorType())) {

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
