var usb = require('usb');

var FAIL_STATUS = 7;
var PROGRESS_STATUS = 6;
var SUCCESS_STATUS = 3;

var device = usb.findByIds(5824, 1500);
var interf;

device.open();
interf = device.interface(0);
interf.claim();

exports.FAIL_STATUS = FAIL_STATUS;
exports.SUCCESS_STATUS = SUCCESS_STATUS;

exports.sendStatus = function(status){
	device.controlTransfer(usb.LIBUSB_REQUEST_TYPE_RESERVED, usb.LIBUSB_TRANSFER_TYPE_CONTROL, status, 0, new Buffer(0), function(error, data) {
		if(error) {
			console.error('USB transfer:', error);
		}
	});
}