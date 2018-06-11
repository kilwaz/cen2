package core.process;

import data.model.objects.Clip;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Encoder implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Encoder.class);
    private Clip clip;
    private ManagedThread pass1ManagedThread;
    private ProcessHelper pass1ProcessHelper;

    private ManagedThread pass2ManagedThread;
    private ProcessHelper pass2ProcessHelper;
    private Integer pass = 1;

    private List<Flow.Subscription> subscriptions = new ArrayList<>();

    private static final String FFMPEG_LOG_FILE_DIR = "/home/kilwaz/srcLog/";
    private static final String FFMPEG_FINAL_ENCODED_DESTINATION = "/home/kilwaz/srcDone/";

    public Encoder() {
        super();
    }

    public Encoder clip(Clip clip) {
        this.clip = clip;
        return this;
    }

    public void execute() {
//        CRF
//        32   lowest
//        29   very low
//        26   low
//        23   medium
//        20   high
//        17   perceptually lossless

        String command = "/usr/bin/ffmpeg -y -i " + clip.getFileName() + " -c:v libvpx-vp9 -c:a libopus -pass 1 -passlogfile " + FFMPEG_LOG_FILE_DIR + clip.getUuidString() + " -b:v 0 -deadline good -crf 17 -f webm /dev/null";

        pass1ProcessHelper = new ProcessHelper()
                .command(command)
                .processDescription("Encoding " + clip.getSource().getFileName())
                .processReference(clip.getUuidString())
                .logSubscriber(this);

        pass1ManagedThread = new ManagedThread()
                .managedRunnable(pass1ProcessHelper)
                .start();

        command = "/usr/bin/ffmpeg -y -i " + clip.getFileName() + " -c:v libvpx-vp9 -c:a libopus -pass 2 -passlogfile " + FFMPEG_LOG_FILE_DIR + clip.getUuidString() + " -b:v 0 -deadline good -crf 17 -f webm " + FFMPEG_FINAL_ENCODED_DESTINATION + "/encoded-" + clip.getUuidString() + "." + clip.getSource().getFileExtension();

        pass2ProcessHelper = new ProcessHelper()
                .command(command)
                .processDescription("Encoding " + clip.getSource().getFileName())
                .processReference(clip.getUuidString())
                .logSubscriber(this);

        pass2ManagedThread = new ManagedThread().managedRunnable(pass2ProcessHelper);
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

            var encodedProgress = clip.getEncodedProgress();
            if (pass.equals(1)) {
                encodedProgress.setPass1Progress(frame);
            } else if (pass.equals(2)) {
                encodedProgress.setPass2Progress(frame);

                File pass1LogFile = new File(FFMPEG_LOG_FILE_DIR + clip.getUuidString() + "-0.log");
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
        if (pass == 1) {
            pass2ManagedThread.start();
        }
    }
}
