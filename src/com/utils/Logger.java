package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

	public enum LogLevel {
		CRITICAL,
		ERROR,
		INFO;

		@Override
		public String toString() {
			return switch (this) {
				case INFO -> "INFO >> ";
				case CRITICAL -> "CRITICAL >> ";
				case ERROR -> "ERROR >> ";
			};
		}
	}

	private BufferedWriter logBW;
	private static Logger instance;

	private Logger() {
		LocalDateTime currDate = LocalDateTime.now();
		String currDateFormated = currDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		long currPID = ProcessHandle.current().pid();

		String formattedLogFileName = "log-" + currDateFormated + "-" + currPID + ".log";
		String systemSeparator = System.getProperty("file.separator");
		String fullPath = "D:" + systemSeparator + "Temp" + systemSeparator;
		try {
			File logFilePath = new File(fullPath);
			logFilePath.mkdirs();
			File logFile = new File(fullPath + formattedLogFileName);
			logFile.createNewFile();

			this.logBW = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static Logger getLogger() {
		if (instance == null) {
			instance = new Logger();
		}

		return instance;
	}

	public void logMessage(LogLevel level, String msg) {
		if (this.logBW == null) {
			System.err.println("Error: Unable to connect to the log file.");
			return;
		}

		LocalDateTime currDate = LocalDateTime.now();
		String currDateFormated = currDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		try {
			logBW.write(level + currDateFormated + " >> " + msg);
			logBW.newLine();
			logBW.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
