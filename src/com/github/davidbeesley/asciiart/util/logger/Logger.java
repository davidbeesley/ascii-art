package com.github.davidbeesley.asciiart.util.logger;

import java.util.concurrent.Semaphore;

/**
 * System wide logger.
 * This logger is meant to be shared among all parts of the program. It is thread safe.
 * It is designed so that output can be easily turned on or off.
 */
public class Logger {

    private  final LogLevel logLevelDefault = LogLevel.INFO;
    private  LogLevel logLevel = logLevelDefault;
    private static Logger instance;
    private Semaphore semaphore;

    /**
     * Singleton
     */
    public static Logger getInstance(){
        if (instance == null){
            instance = new Logger();
        }
        return instance;
    }

    private Logger(){
        semaphore = new Semaphore(1);
    }


    /**
     *
     * @param messageLogLevel
     * @param message
     * @return TRUE if output was printed.
     */
    protected boolean output(LogLevel messageLogLevel, String message){

        if (!logLevel.shouldOutput(messageLogLevel)) return false;

        semaphore.acquireUninterruptibly();
        System.out.print(messageLogLevel.toString() + "\t");


        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();


        int loggerIndex = 0;
        String className = getClassName(stackTraceElements[loggerIndex].getClassName());
        while (!className.equals("Logger")){
            loggerIndex += 1;
            className = getClassName(stackTraceElements[loggerIndex].getClassName());
        }
        loggerIndex += 2;
        className = getClassName(stackTraceElements[loggerIndex].getClassName());





        System.out.print(className + ":" + stackTraceElements[loggerIndex].getMethodName() + "():\t");
        System.out.println(message);
        //System.out.println();
        semaphore.release();

        return true;
    }

    /**
     * Set the global logging level.
     * @param logLevel
     */
    public void setLogLevel(LogLevel logLevel){
        this.logLevel = logLevel;
    }



    public void trace(String message){
        output(LogLevel.TRACE, message);
    }
    public void debug(String message){
        output(LogLevel.DEBUG, message);

    }
    public void info(String message){
        output(LogLevel.INFO, message);

    }
    public void warning(String message){
        output(LogLevel.WARNING, message);

    }
    public void error(String message){
        output(LogLevel.ERROR, message);

    }

    public void restoreDefault(){
        logLevel = logLevelDefault;
    }


    public LogLevel getLogLevel() {
        return logLevel;
    }

    private String getClassName(String s){
        int i = s.lastIndexOf('.');
        s = s.substring(i+1);
        return s;
    }
}
