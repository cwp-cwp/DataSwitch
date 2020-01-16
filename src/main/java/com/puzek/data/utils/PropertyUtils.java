package com.puzek.data.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class PropertyUtils {

	private static Logger logger = LoggerFactory.getLogger(PropertyUtils.class);
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	public static String readServiceProperties(String key) {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("config/service.properties");
		Properties properties = new Properties();
		try {
			properties.load(resourceAsStream);
		} catch (IOException e) {
			logger.warn(df.format(new Date()) + " " + e.getMessage());
		}
		return properties.getProperty(key);
	}

	public static String getPropertyValue(String key, String filePath) {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath); // "config/service.properties"
		Properties properties = new Properties();
		try {
			properties.load(resourceAsStream);
		} catch (IOException e) {
			logger.warn(df.format(new Date()) + " " + e.getMessage());
		}
		return properties.getProperty(key);
	}

	public static List<String> getAllProperty(String filePath) {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath); // "config/service.properties"
		Properties properties = new Properties();
		try {
			properties.load(resourceAsStream);
		} catch (IOException e) {
			logger.warn(df.format(new Date()) + " " + e.getMessage());
		}
		List<String> lists = new ArrayList<>();
		@SuppressWarnings("rawtypes")
		Enumeration en = properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = properties.getProperty(key);
			String tmpStr = key + ":" + value;
			lists.add(tmpStr);
		}
		return lists;
	}
	
}
