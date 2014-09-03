var jenkinsapi = require('jenkins-api');
var usbConnector = require('./usbConnector');

var JOB_NAME = 'DevCI_iterative';
var SUCCESS_STATUS = 'SUCCESS';
var PROGRESS_STATUS = null;
var FAIL_STATUS = 'FAILURE';
var JENKINS_URL = process.env.JENKINS_URL;

var jenkins = jenkinsapi.init(JENKINS_URL);

exports.checkStatus = function checkStatus(){
	jenkins.last_build_info(JOB_NAME, function(err, data){
		var usbStatus;
		var now = new Date();
 		console.log('Staus = ', data.result, now.toLocaleTimeString());
 		
 		usbStatus = data.result == SUCCESS_STATUS ? usbConnector.SUCCESS_STATUS
 		 			: data.result == PROGRESS_STATUS ? usbConnector.PROGRESS_STATUS
 		 			: data.result == FAIL_STATUS ? usbConnector.FAIL_STATUS
 		 			: 1;
 	 		
 	 	usbConnector.sendStatus(usbStatus, function (error, data) {
 	 		if(!error) {
 	 			
 	 		} else {
				console.error('USB transfer:', error);
				usbConnector.connect();
			}
 	 	});

 	 	// if(data.result == SUCCESS_STATUS){
 	 	// 	usbConnector.sendStatus(usbConnector.SUCCESS_STATUS, cb);
 	 	// }else if(data.result == PROGRESS_STATUS){
 	 	// 	usbConnector.sendStatus(usbConnector.PROGRESS_STATUS, cb);
 	 	// }else if(data.result == FAIL_STATUS){
 	 	// 	usbConnector.sendStatus(usbConnector.FAIL_STATUS, cb);
 	 	// }
	});
}