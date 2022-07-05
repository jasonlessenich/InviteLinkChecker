package com.dynxsty.linkchecker.properties;


import com.dynxsty.linkchecker.Constants;

import java.io.*;
import java.util.Properties;


public class BotProperties {
	protected final String entryname;

	protected BotProperties(String entryname) {
		this.entryname = entryname;
	}

	public static boolean init() {
		try {
			return new File(Constants.CONFIG_PATH).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void save(String value) throws IOException {
		Properties prop = new Properties();
		try (BufferedInputStream s = new BufferedInputStream(new FileInputStream(Constants.CONFIG_PATH))) {
			prop.load(s);
		} catch (FileNotFoundException e) {
			init();
			save(value);
		}
		try (FileOutputStream out = new FileOutputStream(Constants.CONFIG_PATH)) {
			prop.setProperty(entryname, value);
			prop.store(out, "");
		}
	}

	public String load() throws IOException {
		try (BufferedInputStream s = new BufferedInputStream(new FileInputStream(Constants.CONFIG_PATH))) {
			Properties prop = new Properties();
			prop.load(s);
			return prop.getProperty(entryname);
		}
	}

	public boolean isRegisteredInConfig() {
		try (BufferedInputStream s = new BufferedInputStream(new FileInputStream(Constants.CONFIG_PATH))) {
			Properties prop = new Properties();
			prop.load(s);
			return prop.containsKey(entryname);
		} catch (Exception e) {
			return false;
		}
	}
}
