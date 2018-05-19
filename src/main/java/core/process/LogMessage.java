package core.process;

public class LogMessage {
    private String message = "";
    private ProcessHelper processHelper;
    private Integer processorType;
    private Boolean isFinalMessage = false;

    public LogMessage processHelper(ProcessHelper processHelper) {
        this.processHelper = processHelper;
        return this;
    }

    public LogMessage message(String message) {
        this.message = message;
        return this;
    }

    public LogMessage processorType(Integer processorType) {
        this.processorType = processorType;
        return this;
    }

    public LogMessage finalMessage(Boolean isFinalMessage) {
        this.isFinalMessage = isFinalMessage;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ProcessHelper getProcessHelper() {
        return processHelper;
    }

    public Integer getProcessorType() {
        return processorType;
    }

    public Boolean isFinalMessage() {
        return isFinalMessage;
    }
}
