package core.process;

import data.model.DatabaseObject;
import data.model.objects.Clip;
import data.model.objects.EncodedProgress;
import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Prober implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Prober.class);
    private List<Source> sources = new ArrayList<>();
    private List<Clip> clips = new ArrayList<>();
    private DatabaseObject currentProbingObject;
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

    public Prober addClip(Clip clip) {
        this.clips.add(clip);
        return this;
    }

    public Prober clips(List<Clip> clips) {
        this.clips.addAll(clips);
        return this;
    }

    public void execute() {
        processNextSource();
    }

    private void processNextSource() {
        String command = "";
        String description = "";
        String reference = "";
        if (sources.size() > 0) {
            currentProbingObject = sources.get(0);
            sources.remove(currentProbingObject);
        } else if (clips.size() > 0) {
            currentProbingObject = clips.get(0);
            clips.remove(currentProbingObject);
        } else {
            return;
        }

        reference = currentProbingObject.getUuidString();

        String fileName = "";

        if (currentProbingObject instanceof Clip) {
            fileName = ((Clip) currentProbingObject).getFileName();
        } else if (currentProbingObject instanceof Source) {
            fileName = ((Source) currentProbingObject).getFileName();
        }

        description = "Probing " + fileName;
//        command = "/usr/bin/ffprobe -v quiet -print_format json -show_format -show_streams " + fileName;

        ProcessParams processParams = new ProcessParams();
        processParams.path("/usr/bin/ffprobe");
        processParams
                .add("-v")
                .add("quiet")
                .add("-print_format")
                .add("json")
                .add("-show_format")
                .add("-show_streams")
                .add(fileName);

        log.info("Prober command = " + processParams.getCommand());

        processHelper = new ProcessHelper()
                .command(processParams)
                .processDescription(description)
                .processReference(reference)
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
                if (currentProbingObject instanceof Clip) {
                    Clip currentProcessingClip = (Clip) currentProbingObject;

                    var jsonContainer = new JSONContainer(jsonResult.toString());
                    var probeJSON = jsonContainer.toJSONObject();

                    processProbeInfo(probeJSON, currentProcessingClip.getEncodedProgress());
                } else if (currentProbingObject instanceof Source) {
                    Source currentProcessingSource = (Source) currentProbingObject;

                    var jsonContainer = new JSONContainer(jsonResult.toString());
                    var probeJSON = jsonContainer.toJSONObject();
                    currentProcessingSource.setSourceInfo(probeJSON);
                    currentProcessingSource.save();

                    processProbeInfo(probeJSON, currentProcessingSource.getEncodedProgress());
                }
            } else {
                jsonResult.append(item.getMessage());
            }
        }

        for (Flow.Subscription subscription : subscriptions) {
            subscription.request(1);
        }
    }

    private void processProbeInfo(JSONObject probeJSON, EncodedProgress encodedProgress) {
        // Find the total number of frames calculated from average frame rate and duration
        if (probeJSON.has("streams")) {
            var streamArray = probeJSON.getJSONArray("streams");
            if (streamArray.length() > 0) {
                for (int i = 0; i < streamArray.length(); i++) {
                    var videoStreamJSON = streamArray.getJSONObject(i);

                    if (videoStreamJSON.has("codec_type") && "video".equals(videoStreamJSON.getString("codec_type"))) {

                        if (videoStreamJSON.has("avg_frame_rate") && videoStreamJSON.has("duration")) {
                            var avgFrameRate = videoStreamJSON.getString("avg_frame_rate");
                            var duration = videoStreamJSON.getDouble("duration");
                            var frameRateSplit = avgFrameRate.split("/");

                            if (frameRateSplit.length == 2) {
                                var firstSum = Double.parseDouble(frameRateSplit[0]);
                                var secondSum = Double.parseDouble(frameRateSplit[1]);

                                Double totalFrames = duration * (firstSum / secondSum);
                                log.info(duration + " * " + firstSum + "/" + secondSum + " = " + (firstSum / secondSum) + " = " + totalFrames);
                                encodedProgress.setTotalFrames(totalFrames.intValue());
                                encodedProgress.save();

                                processNextSource();
                            }
                        }
                    }
                }
            }
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
