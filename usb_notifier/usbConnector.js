var usb = require('usb');

var FAIL_STATUS = 30;
var PROGRESS_STATUS = 21;
var SUCCESS_STATUS = 40;


exports.FAIL_STATUS = FAIL_STATUS;
exports.PROGRESS_STATUS = PROGRESS_STATUS;
exports.SUCCESS_STATUS = SUCCESS_STATUS;

exports.sendStatus = function(status, done){
	var interf;
	var device;

	device = usb.findByIds(5824, 1500);

	if(device) {
		device.open();
		// interf = device.interface(0);
		// interf.claim();	
		
		device.controlTransfer(usb.LIBUSB_REQUEST_TYPE_RESERVED, usb.LIBUSB_TRANSFER_TYPE_CONTROL, status, 0, new Buffer(0), function(error, data) {
			done(error, data);
			device.close();
		});
	} else {
		done(new Error('Device is not found.'));
	}
}