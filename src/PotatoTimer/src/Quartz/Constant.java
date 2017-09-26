package Quartz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class Constant {
	private static Constant cons = new Constant();
	private static Properties pro;

	private Constant() {
		pro = new Properties();
		try {
			pro.load(new InputStreamReader(new FileInputStream("constant.properties"), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final int WIN_WIDTH = Integer.parseInt(pro.getProperty("WIN_WIDTH").trim());
	public static final int WIN_HEIGHT = Integer.parseInt(pro.getProperty("WIN_HEIGHT").trim());
	public static final int DIAWIN_WIDTH = Integer.parseInt(pro.getProperty("DIAWIN_WIDTH").trim());
	public static final int DIAWIN_HEIGHT = Integer.parseInt(pro.getProperty("DIAWIN_HEIGHT").trim());
	public static final int TIME_LONG = Integer.parseInt(pro.getProperty("TIME_LONG").trim());
	public static final int REST_TIME_LONG = Integer.parseInt(pro.getProperty("REST_TIME_LONG").trim());
	public static final int LONGREST_TIME_LONG = Integer.parseInt(pro.getProperty("LONGREST_TIME_LONG").trim());
	public static final String TIME_FORMAT = pro.getProperty("TIME_FORMAT").trim();

	public static String BURNPLAN;

	private static void initConfig() throws ParseException {
		BURNPLAN = pro.getProperty("BURNPLAN").trim();
		BURNPLAN = BURNPLAN.substring(8, BURNPLAN.length());
	}

	public static Constant getInstance(Map<String, String> params) {
		if (params == null) {
			try {
				initConfig();
				initLang();
				langVals(LANGs[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				String burnplan = params.get("burnplan");
				if (burnplan != null) {
					pro.setProperty("BURNPLAN", new SimpleDateFormat("yyyyMMdd").format(new Date()) + burnplan);
				}

				String lang = params.get("lang");
				if (lang == null) {
					langVals(LANGs[0]);
				} else {
					langVals(lang);
					StringBuffer str = new StringBuffer();
					str.append(lang);
					for (int i = 0; i < LANGs.length; i++) {
						if (!LANGs[i].equals(lang)) {
							str.append(",").append(LANGs[i]);
						}
					}
					pro.setProperty("LANG", str.toString());
				}

				pro.store(new OutputStreamWriter(new FileOutputStream("constant.properties"), "utf-8"), null);
				initConfig();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return cons;
	}

	public static String[] LANGs;

	public static String TITLE;
	public static String DIALOG_TITLE;
	public static String ALERT;
	public static String RATE;
	public static String BTN_START;
	public static String BTN_CONTINUE;
	public static String BTN_END;
	public static String BTN_ABORT;
	public static String BTN_ADD;
	public static String BTN_SVE;
	public static String MENU_NEW;
	public static String MENU_LOG;
	public static String MENU_LANG;
	public static String END_CONFIRM;
	public static String ABORT_CONFIRM;
	public static String NO_LOG;
	public static String NO_PLAN;

	private static void initLang() {
		String[] strs = pro.getProperty("LANG").split(",");
		LANGs = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			LANGs[i] = strs[i].trim();
		}
	}

	private static void langVals(String lang) {
		TITLE = pro.getProperty(lang + "_TITLE");
		DIALOG_TITLE = pro.getProperty(lang + "_DIALOG_TITLE");
		ALERT = pro.getProperty(lang + "_ALERT");
		RATE = pro.getProperty(lang + "_RATE");
		BTN_START = pro.getProperty(lang + "_BTN_START");
		BTN_CONTINUE = pro.getProperty(lang + "_BTN_CONTINUE");
		BTN_END = pro.getProperty(lang + "_BTN_FINISH");
		BTN_ABORT = pro.getProperty(lang + "_BTN_ABORT");
		BTN_ADD = pro.getProperty(lang + "_BTN_ADD");
		BTN_SVE = pro.getProperty(lang + "_BTN_SVE");
		MENU_NEW = pro.getProperty(lang + "_MENU_NEW");
		MENU_LOG = pro.getProperty(lang + "_MENU_LOG");
		MENU_LANG = pro.getProperty(lang + "_MENU_LANG");
		END_CONFIRM = pro.getProperty(lang + "_END_CONFIRM");
		ABORT_CONFIRM = pro.getProperty(lang + "_ABORT_CONFIRM");
		NO_LOG = pro.getProperty(lang + "_NO_LOG");
		NO_PLAN = pro.getProperty(lang + "_NO_PLAN");
	}

}
