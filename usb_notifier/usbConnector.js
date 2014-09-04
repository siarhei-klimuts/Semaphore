var usb = require('usb');
var device;

exports.FAIL_STATUS = 30;
exports.PROGRESS_STATUS = 21;
exports.SUCCESS_STATUS = 40;

function connect() {
	device && device.close();
	device = usb.findByIds(5824, 1500);
	device && device.open();
	
	console.log('Device connected');
}

exports.connect = connect;

exports.sendStatus = function(status, done){
	try {
		device.controlTransfer(usb.LIBUSB_REQUEST_TYPE_RESERVED, usb.LIBUSB_TRANSFER_TYPE_CONTROL, status, 0, new Buffer(0), function(error, data) {
			done(error, data);
		});
	} catch(exception) {
		done(new Error(exception));
	}
}

connect();