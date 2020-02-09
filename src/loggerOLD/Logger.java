package loggerOLD;

/**
 * System wide logger.
 * This logger is meant to be shared among all parts of the program. It is not currently thread safe.
 * It is designed so that output can be easily turned on or of.
 */
public class Logger {

    private static final LogLevel logLevelDefault = LogLevel.TRACE;
    protected static LogLevel logLevel = logLevelDefault;


    /**
     *
     * @param logLevel
     * @param message
     * @return TRUE if output was printed.
     */
    protected static boolean output(LogLevel logLevel, String message){

        if (Logger.logLevel.shouldNotOutput(logLevel))return false;
        if (logLevel == LogLevel.NONE) return false;

        System.out.print(logLevel.toString() + "\t");


        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();


        int index = 3;

        System.out.print(stackTraceElements[index].getClassName() + ":" + stackTraceElements[index].getMethodName() + "():\t");
        System.out.println(message);
        System.out.println();

        return true;
    }

    protected static boolean outputNoTrace(LogLevel logLevel, String message){

        if (Logger.logLevel.shouldNotOutput(logLevel))return false;
        if (logLevel == LogLevel.NONE) return false;
        System.out.println(message);
        System.out.println();

        return true;
    }


    /**
     * Set the global logging level.
     * @param logLevel
     */
    public static void setLogLevel(LogLevel logLevel){
        Logger.logLevel = logLevel;
    }



    public static void trace(String message){
        output(LogLevel.TRACE, message);
    }
    public static void debug(String message){
        output(LogLevel.DEBUG, message);

    }
    public static void info(String message){
        output(LogLevel.INFO, message);

    }
    public static void message(String message){
        outputNoTrace(LogLevel.WARNING, message);
    }
    public static void warning(String message){
        output(LogLevel.WARNING, message);

    }
    public static void error(String message){
        output(LogLevel.ERROR, message);

    }

    public static void restoreDefault(){
        logLevel = logLevelDefault;
    }


}
