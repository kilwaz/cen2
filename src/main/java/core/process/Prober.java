package core.process;

import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Prober implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Prober.class);
    private List<Source> sources = new ArrayList<>();
    private Source currentProcessingSource;
    private ManagedThread managedThread;
    private ProcessHelper processHelper;
    private StringBuilder jsonResult = new StringBuilder();

    private List<Flow.Subscription> subscriptions = new ArrayList<>();

    public Prober() {
        super();
    }

    public Prober addSource(Source source) {
        this.sources.add(source);
        return this;
    }

    public Prober sources(List<Source> sources) {
        this.sources.addAll(sources);
        return this;
    }

    public void execute() {
        processNextSource();
    }

    private void processNextSource() {
        if (sources.size() > 0) {
            currentProcessingSource = sources.get(0);
            sources.remove(currentProcessingSource);

            String command = "/usr/bin/ffprobe -v quiet -print_format json -show_format -show_streams " + currentProcessingSource.getFileName();

            log.info("Prober command = " + command);

            processHelper = new ProcessHelper()
                    .command(command)
                    .processDescription("Probing " + currentProcessingSource.getFileName())
                    .processReference(currentProcessingSource.getUuidString())
                    .logSubscriber(this);

            managedThread = new ManagedThread()
                    .managedRunnable(processHelper)
                    .start();
        }
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
                log.info("Final message for " + currentProcessingSource.getName());
                var jsonContainer = new JSONContainer(jsonResult.toString());
                var probeJSON = jsonContainer.toJSONObject();
                currentProcessingSource.setSourceInfo(probeJSON);
                currentProcessingSource.save();

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
                                    var encodedProgress = currentProcessingSource.getEncodedProgress();
                                    encodedProgress.setTotalFrames(totalFrames.intValue());
                                    encodedProgress.save();

                                    //processHelper.unsubscribeAll();
                                    processNextSource();
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
