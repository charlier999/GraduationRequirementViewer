package com.gradview.service;

import java.util.ArrayList;
import java.util.List;

public class ComponentLogger 
{
    private String fileLocation;
    private List<LogMessage> logs;
    public ComponentLogger(String fileLocation)
    {
        this.fileLocation = fileLocation;
        this.logs = new ArrayList<>();
    }

    /**
     * Logs a message to local sotrage
     * @param methodLocation What function/method the log is coming from.
     * @param message The message to be stored.
     */
    public void dev(String methodLocation, String message)
    {
        LogMessage logMessage = new LogMessage();
        logMessage.fileLocation = this.fileLocation;
        logMessage.messageLevel = LogMessage.LEVEL_DEV;
        logMessage.methodLocation = methodLocation;
        logMessage.message = message;
        this.logs.add(logMessage);
    }

    /**
     * Logs a message to local sotrage
     * @param methodLocation What function/method the log is coming from.
     * @param message The message to be stored.
     */
    public void info(String methodLocation, String message)
    {
        LogMessage logMessage = new LogMessage();
        logMessage.fileLocation = this.fileLocation;
        logMessage.messageLevel = LogMessage.LEVEL_INFO;
        logMessage.methodLocation = methodLocation;
        logMessage.message = message;
        this.logs.add(logMessage);
    }

    /**
     * Logs a message to local sotrage
     * @param methodLocation What function/method the log is coming from.
     * @param message The message to be stored.
     */
    public void warn(String methodLocation, String message)
    {
        LogMessage logMessage = new LogMessage();
        logMessage.fileLocation = this.fileLocation;
        logMessage.messageLevel = LogMessage.LEVEL_WARN;
        logMessage.methodLocation = methodLocation;
        logMessage.message = message;
        this.logs.add(logMessage);
    }

    /**
     * Logs a message to local sotrage
     * @param methodLocation What function/method the log is coming from.
     * @param message The message to be stored.
     */
    public void error(String methodLocation, String message)
    {
        LogMessage logMessage = new LogMessage();
        logMessage.fileLocation = this.fileLocation;
        logMessage.messageLevel = LogMessage.LEVEL_ERROR;
        logMessage.methodLocation = methodLocation;
        logMessage.message = message;
        this.logs.add(logMessage);
    }

    /**
     * Retrieves all logs stored.
     * @return
     */
    public List<LogMessage> getLogs()
    {
        return this.logs;
    }

    /**
     * Clears logs from storage.
     */
    public void clear()
    {
        this.logs.clear();
    }
}



