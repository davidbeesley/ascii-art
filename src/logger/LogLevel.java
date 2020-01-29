package logger;

/**
 * Valid Logger Levels.
 */
public enum LogLevel {
    TRACE(0), DEBUG(1), INFO(2), WARNING(3), ERROR(4), NONE(5);



    private Integer level;

    LogLevel(int level) {
        this.level = level;
    }

    /**
     * For comparing a message loglevel to the current log level
     * @param other message loglevel
     * @return true if the message loglevel is important enough to print.
     */
    public boolean shouldNotOutput(LogLevel other) {
        return this.level > other.level;
    }
}
