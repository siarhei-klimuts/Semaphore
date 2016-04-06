package com.github.galiaf47.semaphore;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class Tray {
    private MenuItem statusItem;
    private TrayIcon trayIcon;
    private boolean exit;
    
	public Tray() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            exit = true;
            return;
        }
        
        exit = false;
        PopupMenu popup = new PopupMenu();
        ImageIcon icon = new ImageIcon(Tray.class.getResource("/green.png"));
        trayIcon = new TrayIcon(icon.getImage());
        SystemTray tray = SystemTray.getSystemTray();
       
        statusItem = new MenuItem("Status");
        statusItem.setEnabled(false);
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				exit = true;
			}
		});
        
        popup.add(statusItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
		trayIcon.setToolTip(Config.JENKINS_JOB);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
	}
	
	public void remove() { 
		SystemTray.getSystemTray().remove(trayIcon);
	}
	
	public void setStatus(String status) {
		statusItem.setLabel(status);
		trayIcon.setImage(Config.getIcon(status));
	}
	
	public void setError(String msg) {
		statusItem.setLabel(msg);
		trayIcon.setImage(Config.getErrorIcon());
	}
	
	public boolean isExit() {
		return exit;
	}
}
