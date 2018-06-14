package core.process;

import data.model.objects.Clip;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Splitter implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Splitter.class);
    private Clip clip;
    private ManagedThread managedThread;
    private ProcessHelper processHelper;
    private StringBuilder jsonResult = new StringBuilder();

    private List<Flow.Subscription> subscriptions = new ArrayList<>();

    public Splitter() {
        super();
    }

    public Splitter clip(Clip clip) {
        this.clip = clip;
        return this;
    }

    private String convertTimeToString(Double timeInSeconds) {
        Double timeHour = Math.floor(timeInSeconds / 3600);
        Double timeMin = Math.floor((timeInSeconds / 60) % 60);
        Double timeSec = Math.floor(timeInSeconds % 60);

        return String.format("%02d", timeHour.intValue()) + ":" + String.format("%02d", timeMin.intValue()) + ":" + String.format("%02d", timeSec.intValue());
    }

    public void execute() {
        clip.setFileName("/home/kilwaz/srcDone/clip-" + clip.getUuidString() + "." + clip.getSource().getFileExtension());
        clip.save();

        Double startTime = clip.getStartMark().getTime();
        Double endTime = clip.getEndMark().getTime();

//        String command = "/usr/bin/ffmpeg -ss " + convertTimeToString(startTime) + " -i " + clip.getSource().getFileName() + " -to " + convertTimeToString(endTime - startTime) + " -acodec copy -vcodec copy -async 1 -y -force_key_frames 00:00:00.000 " + clip.getFileName();

        ProcessParams processParams = new ProcessParams();
        processParams.path("/usr/bin/ffmpeg");
        processParams
                .add("-ss")
                .add(convertTimeToString(startTime))
                .add("-i")
                .add(clip.getSource().getFileName())
                .add("-to")
                .add(convertTimeToString(endTime - startTime))
                .add("-acodec")
                .add("copy")
                .add("-vcodec")
                .add("copy")
                .add("-async")
                .add("1")
                .add("-y")
                .add("-force_key_frames")
                .add("00:00:00.00")
                .add(clip.getFileName());

        log.info("Splitter command = " + processParams.getCommand());

        processHelper = new ProcessHelper()
                .command(processParams)
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
        Prober prober = new Prober();
        prober.addClip(clip);
        prober.execute();
    }
}
