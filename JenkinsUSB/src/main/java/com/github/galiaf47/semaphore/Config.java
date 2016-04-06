package com.github.galiaf47.semaphore;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

public class Config {
	private static Properties prop;
	private static final String FILE_NAME = "config.properties";
	
	private static final Map<String, Short> statusMap = new HashMap<String, Short>();
	private static final Map<String, Image> iconMap = new HashMap<String, Image>();
	private static final ImageIcon errorIcon = new ImageIcon(Tray.class.getResource("/error.png"));
	
	static {
		loadConfig();
	}
	
	public static String JENKINS_URL = prop.getProperty("jenkins.url", "http://localhost:8080/jenkins/");
	public static String JENKINS_LOGIN = prop.getProperty("jenkins.login", "admin");
	public static String JENKINS_PASSWORD = prop.getProperty("jenkins.password", "admin");
	public static String JENKINS_JOB = prop.getProperty("jenkins.job", "my_job");
	
	public static String JENKINS_STATUS_FAILURE = prop.getProperty("jenkins.status.failure", "Failure");
	public static String JENKINS_STATUS_PENDING = prop.getProperty("jenkins.status.pending", "Pending");
	public static String JENKINS_STATUS_SUCCESS = prop.getProperty("jenkins.status.success", "Success");
	
	public static boolean USB_ENABLED = Boolean.valueOf(prop.getProperty("usb.enabled", "false"));
	public static short USB_STATUS_EXCEPTION = Short.valueOf(prop.getProperty("usb.status.exception", "31"));
	public static short USB_STATUS_CONNECTED = Short.valueOf(prop.getProperty("usb.status.connected", "20"));
	public static short USB_STATUS_DISCONNECTED = Short.valueOf(prop.getProperty("usb.status.disconnected", "0"));
	public static short USB_STATUS_FAILURE = Short.valueOf(prop.getProperty("usb.status.build.failure", "30"));
	public static short USB_STATUS_PENDING = Short.valueOf(prop.getProperty("usb.status.build.pending", "21"));
	public static short USB_STATUS_SUCCESS = Short.valueOf(prop.getProperty("usb.status.build.success", "40"));
	
	static {
		ImageIcon greenIcon = new ImageIcon(Tray.class.getResource("/green.png"));
		ImageIcon yellowIcon = new ImageIcon(Tray.class.getResource("/yellow.png"));
		ImageIcon redIcon = new ImageIcon(Tray.class.getResource("/red.png"));
		
		statusMap.put(JENKINS_STATUS_FAILURE, USB_STATUS_FAILURE);
		statusMap.put(JENKINS_STATUS_PENDING, USB_STATUS_PENDING);
		statusMap.put(JENKINS_STATUS_SUCCESS, USB_STATUS_SUCCESS);
		
		iconMap.put(JENKINS_STATUS_FAILURE, redIcon.getImage());
		iconMap.put(JENKINS_STATUS_PENDING, yellowIcon.getImage());
		iconMap.put(JENKINS_STATUS_SUCCESS, greenIcon.getImage());
	}

	public static short getUSBCode(String status) {
		return statusMap.get(status);
	}
	
	public static Image getIcon(String status) {
		return iconMap.get(status);
	}
	
	public static Image getErrorIcon() {
		return errorIcon.getImage();
	}
	
	private static void loadConfig() { 
		InputStream input;
		prop = new Properties();
		
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			input = loader.getResourceAsStream(FILE_NAME);
			prop.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
