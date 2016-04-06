package com.github.galiaf47.semaphore;

import java.nio.ByteBuffer;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class UsbService {
	private static final byte BM_REQUEST_TYPE = (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE);
	private static final byte B_REQUEST  = 0x09;
	private static final short W_INDEX  = 1;
	private static final long TIMEOUT  = 0;
	private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(0);
	
	private Context context;
	private Device device;
	
	private short lastStatus;
	
	public void connect() {
		if (!Config.USB_ENABLED) return;
		
		context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);
		
		device = findDevice(5824, 1500);
		send(device, Config.USB_STATUS_CONNECTED);
	}
	
	public void disconnect() {
		if (!Config.USB_ENABLED) return;
		
		send(device, Config.USB_STATUS_DISCONNECTED);
		LibUsb.exit(context);
	}
	
	public boolean sendStatus(short status) {
		if (status != lastStatus) {
			lastStatus = status;
			send(device, status);
			
			return true;
		}
		
		return false;
	}
	
	private static Device findDevice(int vendorId, int productId) {
		Device result = null;
		
	    DeviceList list = new DeviceList();
	    int deviceListResult = LibUsb.getDeviceList(null, list);
	    if (deviceListResult < 0) throw new LibUsbException("Unable to get device list", deviceListResult);

        for (Device device: list) {
            DeviceDescriptor descriptor = new DeviceDescriptor();
            int descriptorResult = LibUsb.getDeviceDescriptor(device, descriptor);
            
            if (descriptorResult != LibUsb.SUCCESS) {
            	System.err.println("Unable to read device descriptor");
            	continue;
            }
            
        	if (descriptor.idVendor() == vendorId && descriptor.idProduct() == productId) {
        		result = device;
        		break;
        	}
        }
        
        LibUsb.freeDeviceList(list, false);

	    return result;
	}
	
	private static void send(Device device, short status) {
		if (!Config.USB_ENABLED) return;
		
		DeviceHandle handle = new DeviceHandle();
		int openResult = LibUsb.open(device, handle);
		
		try	{
			if (openResult != LibUsb.SUCCESS) throw new LibUsbException("Unable to open USB device", openResult);
			LibUsb.controlTransfer(handle, BM_REQUEST_TYPE, B_REQUEST, status, W_INDEX, BUFFER, TIMEOUT);
		} finally {
		    LibUsb.close(handle);
		}
	}
}
