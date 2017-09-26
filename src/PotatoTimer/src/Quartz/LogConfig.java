package Quartz;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogConfig {
	private Logger logger;
	private SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT);
	public static String saveBase = "logs/";

	public LogConfig() {
		logger = getLogger();
	}
	
	public Logger getLogger() {
		Logger logger = null;
		String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".log";
		try {
			FileHandler fh = new FileHandler(saveBase + filename, true);
			fh.setFormatter(new Formatter() {
				
				@Override
				public String format(LogRecord lr) {
					StringBuffer msg = new StringBuffer();
					msg.append(lr.getMessage());
					msg.append("\n");
					return msg.toString();
				}
			});
			
			logger = Logger.getLogger(saveBase + filename);
			logger.setLevel(Level.ALL);
			logger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		return logger;
	}
	
	public void logbegin(String theme) {
		logger.info(theme + " BEGIN: " + sdf.format(new Date()));
	}
	
	public void logend(String theme, String rate) {
		logger.info(theme + " FINISH: " + sdf.format(new Date()) + " <" + rate + ">\n");
	}
	
	public void logabort(String theme) {
		logger.info(theme + " ABORT: " + sdf.format(new Date()));
	}
	
	public void logplan(String burnplan) {
		logger.info(burnplan);
	}
}
