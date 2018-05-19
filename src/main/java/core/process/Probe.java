package core.process;

import data.model.objects.Source;
import data.model.objects.json.JSONContainer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Probe implements Flow.Subscriber<LogMessage> {
    private static Logger log = Logger.getLogger(Probe.class);
    private Source source;
    private ManagedThread managedThread;
    private ProcessHelper processHelper;
    private StringBuilder jsonResult = new StringBuilder();

    private List<Flow.Subscription> subscriptions = new ArrayList<>();

    public Probe() {
        super();
    }

    public Probe source(Source source) {
        this.source = source;
        return this;
    }

    public void execute() {
        String command = "/usr/bin/ffprobe -v quiet -print_format json -show_format -show_streams " + source.getFileName();

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
                source.setSourceInfo(jsonContainer.toJSONObject());
                source.save();
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
