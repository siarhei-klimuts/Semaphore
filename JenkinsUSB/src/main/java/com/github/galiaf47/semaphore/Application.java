package com.github.galiaf47.semaphore;

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
	private short currentStatus;
	
	public Application() throws URISyntaxException {
		usb = new UsbService();
		jenkins = new JenkinsService();
		
		usb.connect();
		jenkins.connect();
	}
	
	public static void main(String[] args) throws URISyntaxException {
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
		String buildStatus = jenkins.getStatus();
		if (buildStatus == null) return;
		
		short usbStatus = statusMap.get(buildStatus);
		if (usbStatus != currentStatus) {
			usb.sendStatus(usbStatus);
			currentStatus = usbStatus;
			System.out.println(new java.util.Date() + ": " + buildStatus);
		}
	}
}
