package de.bright_side.lgf.pc.base;

import de.bright_side.lgf.base.LLogger;

/**
 * Created by me on 14.03.2015.
 */
public class LPcLogger implements LLogger {
    public enum LogLevel {DEBUG, INFO, WARNING, ERROR};

    @Override
    public void debug(String message) {
        logAndGenerateLocation(LogLevel.DEBUG, message);
    }

	@Override
	public void error(Throwable t) {
		if (t != null) {
			t.printStackTrace();
		}
		logAndGenerateLocation(LogLevel.ERROR, "Error: " + t);
	}

    
    private static void logAndGenerateLocation(LogLevel logLevel, String message){
        StackTraceElement[] ste = new Exception().getStackTrace();
        if (ste.length < 3) log(logLevel, "?", "?", 0, message);
        else {
            StackTraceElement e = ste[2];
            if (e != null){
                String fileName = e.getFileName();
                String useFileName = null;
                if (fileName == null){
                    useFileName = "" + ste[2];
                    int lastDotIndex = useFileName.lastIndexOf(".");
                    if (lastDotIndex >= 0) useFileName = useFileName.substring(0, lastDotIndex);
                } else if (fileName.length() > 5){
                    useFileName = fileName.substring(0, fileName.length() - 5);
                } else useFileName = fileName;
                log(logLevel, useFileName, e.getMethodName(), e.getLineNumber(), message);
            }
            else log(logLevel, "???", "???", -1, message);
        }
    }

    private static void log(LogLevel logLevel, String fileName, String methodName, int lineNumber, String message) {
        System.out.println(logLevel + ": " + fileName + "." + methodName + ":" + lineNumber + ">" + message);
    }


}
