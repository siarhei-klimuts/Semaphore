package com.github.galiaf47.semaphore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static final String FILE_NAME = "config.properties";
	private static Properties prop;
	
	static {
		loadConfig();
	}
	
	public static String getJenkinsUrl() {
		return prop.getProperty("jenkins.url");
	}
	
	public static String getJenkinsLogin() {
		return prop.getProperty("jenkins.login");
	}
	
	public static String getJenkinsPassword() {
		return prop.getProperty("jenkins.password");
	}
	
	public static String getJenkinsJob() {
		return prop.getProperty("jenkins.job");
	}
	
	private static void loadConfig() { 
		InputStream input;
		prop = new Properties();
		
		try {
			input = new FileInputStream(FILE_NAME);
			prop.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
