package com.github.galiaf47.semaphore;

import java.io.IOException;
import java.net.URISyntaxException;

public class Application {
	private static final long INTERVAL = 1000;

	private UsbService usb;
	private JenkinsService jenkins;
	private Tray tray;
	
	public Application() {
		usb = new UsbService();
		jenkins = new JenkinsService();
		tray = new Tray();
		
		usb.connect();
		
		try {
			jenkins.connect();
		} catch (URISyntaxException e) {
			logException(e);
		}
	}
	
	public void exit() {
		usb.disconnect();
		jenkins.disconnect();
		tray.remove();
	}
	
	public boolean isRunning() {
		return !tray.isExit();
	}
	
	public static void main(String[] args) {
		Application app = new Application();
		
		while (app.isRunning()) {
			app.update();
			
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		app.exit();
	}
	
	private void update() {
		String buildStatus = null;
		
		try {
			buildStatus = jenkins.getStatus();
		} catch (IOException e) {
			logException(e);
		}
		
		if (buildStatus == null) return;
		
		short usbStatus = Config.getUSBCode(buildStatus);
		if (usb.sendStatus(usbStatus)) {
			tray.setStatus(buildStatus);
			System.out.println(new java.util.Date() + ": " + buildStatus);
		}
	}
	
	private void logException(Exception e) {
		usb.sendStatus(Config.USB_STATUS_EXCEPTION);
		tray.setError(e.getMessage());
		
		e.printStackTrace();
	}
}
