var jenkinsapi = require('jenkins-api');
var usbConnector = require('./usbConnector');
var config = require('./config.js');

var STATUS_MAPPING = {
	'SUCCESS': usbConnector.SUCCESS_STATUS,
	'null': usbConnector.PROGRESS_STATUS,
	'FAILURE': usbConnector.FAIL_STATUS
};

var JOB_NAME = 'DevCI_iterative';
var jenkins = jenkinsapi.init(config.JENKINS_URL);

var previousStatus;

exports.checkStatus = function checkStatus(){
	jenkins.last_build_info(JOB_NAME, function(err, data){
		var usbStatus;
		var now = new Date();
		
 		if(data.result !== previousStatus) {
	 	 	usbConnector.sendStatus(STATUS_MAPPING[data.result], function (error, result) {
	 	 		if(!error) {
 					console.log('Status changed:', previousStatus, data.result, now.toLocaleTimeString());
	 	 			previousStatus = data.result;
	 	 		} else {
					console.error('USB transfer:', error, now.toLocaleTimeString());
					usbConnector.connect();
				}
	 	 	});
	 	}
	});
}