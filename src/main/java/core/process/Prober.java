package core.process;

import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Prober implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Prober.class);
    private Source source;
    private ManagedThread managedThread;
    private ProcessHelper processHelper;
    private StringBuilder jsonResult = new StringBuilder();

    private List<Flow.Subscription> subscriptions = new ArrayList<>();

    public Prober() {
        super();
    }

    public Prober source(Source source) {
        this.source = source;
        return this;
    }

    public void execute() {
        String command = "/usr/bin/ffprobe -v quiet -print_format json -show_format -show_streams " + source.getFileName();

        log.info("Prober command = " + command);

        processHelper = new ProcessHelper()
                .command(command)
                .processDescription("Probing " + source.getFileName())
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
        if (ProcessListener.PROCESSOR_INPUT.equals(item.getProcessorType())) {
            if (item.isFinalMessage()) {
                var jsonContainer = new JSONContainer(jsonResult.toString());
                var probeJSON = jsonContainer.toJSONObject();
                source.setSourceInfo(probeJSON);
                source.save();

                // Find the total number of frames calculated from average frame rate and duration
                if (probeJSON.has("streams") && probeJSON.has("format")) {
                    var formatJSON = probeJSON.getJSONObject("format");
                    var streamArray = probeJSON.getJSONArray("streams");

                    if (formatJSON.has("duration")) {
                        var duration = formatJSON.getDouble("duration");

                        if (streamArray.length() > 0) {
                            var videoStreamJSON = streamArray.getJSONObject(0);
                            if (videoStreamJSON.has("avg_frame_rate")) {
                                var avgFrameRate = videoStreamJSON.getString("avg_frame_rate");
                                var frameRateSplit = avgFrameRate.split("/");

                                if (frameRateSplit.length == 2) {
                                    var firstSum = Double.parseDouble(frameRateSplit[0]);
                                    var secondSum = Double.parseDouble(frameRateSplit[1]);

                                    Double totalFrames = duration * (firstSum / secondSum);
                                    log.info(duration + " * " + firstSum + "/" + secondSum + " = " + (firstSum / secondSum) + " = " + totalFrames);
                                    var encodedProgress = source.getEncodedProgress();
                                    encodedProgress.setTotalFrames(totalFrames.intValue());
                                    encodedProgress.save();
                                }
                            }
                        }
                    }
                }
            } else {
                jsonResult.append(item.getMessage());
            }
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
