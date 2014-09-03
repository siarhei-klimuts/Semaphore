var usb = require('usb');
var device;

exports.FAIL_STATUS = 30;
exports.PROGRESS_STATUS = 21;
exports.SUCCESS_STATUS = 40;

function connect() {
	// device && device.close();
	device = usb.findByIds(5824, 1500);
	device && device.open();
}

exports.connect = connect;

exports.sendStatus = function(status, done){
	if(device) {
		device.controlTransfer(usb.LIBUSB_REQUEST_TYPE_RESERVED, usb.LIBUSB_TRANSFER_TYPE_CONTROL, status, 0, new Buffer(0), function(error, data) {
			done(error, data);
		});
	} else {
		done(new Error('Device is not found.'));
	}
}

connect();