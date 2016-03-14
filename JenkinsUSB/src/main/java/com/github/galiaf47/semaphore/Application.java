package com.github.galiaf47.semaphore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Application {
	private static final long INTERVAL = 1000;
	private static final Map<String, Short> statusMap = new HashMap<String, Short>();
	static {
		statusMap.put(JenkinsService.STATUS_PENDING, UsbService.PROGRESS_STATUS);
		statusMap.put(JenkinsService.STATUS_SUCCESS, UsbService.SUCCESS_STATUS);
		statusMap.put(JenkinsService.STATUS_FAILURE, UsbService.FAIL_STATUS);
	}

	private UsbService usb;
	private JenkinsService jenkins;
	
	public Application() {
		usb = new UsbService();
		jenkins = new JenkinsService();
		
		usb.connect();
		
		try {
			jenkins.connect();
		} catch (URISyntaxException e) {
			logException(e);
		}
	}
	
	public static void main(String[] args) {
		Application app = new Application();
		
		while (true) {
			app.update();
			
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void update() {
		String buildStatus = null;
		
		try {
			buildStatus = jenkins.getStatus();
		} catch (IOException e) {
			logException(e);
		}
		
		if (buildStatus == null) return;
		
		short usbStatus = statusMap.get(buildStatus);
		if (usb.sendStatus(usbStatus)) {
			System.out.println(new java.util.Date() + ": " + buildStatus);
		}
	}
	
	private void logException(Exception e) {
		usb.sendStatus(UsbService.EXCEPTION_STATUS);
		e.printStackTrace();
	}
}
