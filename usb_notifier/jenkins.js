var jenkinsapi = require('jenkins-api');
var usbConnector = require('./usbConnector');

var JOB_NAME = 'DevCI_iterative';
var SUCCESS_STATUS = 'SUCCESS';
var PROGRESS_STATUS = null;
var FAIL_STATUS = 'FAILURE';
var JENKINS_URL = process.env.JENKINS_URL;

var jenkins = jenkinsapi.init(JENKINS_URL);
function cb(error, data) {

		if(error) {
			console.error('USB transfer:', error);
		}
}
exports.checkStatus = function checkStatus(){
	jenkins.last_build_info(JOB_NAME, function(err, data){
		var now = new Date();
 		console.log('Staus = ', data.result, now.toLocaleTimeString());
 		
 	 	if(data.result == SUCCESS_STATUS){
 	 		usbConnector.sendStatus(usbConnector.SUCCESS_STATUS, cb);
 	 	}else if(data.result == PROGRESS_STATUS){
 	 		usbConnector.sendStatus(usbConnector.PROGRESS_STATUS, cb);
 	 	}else if(data.result == FAIL_STATUS){
 	 		usbConnector.sendStatus(usbConnector.FAIL_STATUS, cb);
 	 	}
	});
}