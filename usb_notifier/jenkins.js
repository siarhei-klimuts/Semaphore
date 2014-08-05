var jenkinsapi = require('jenkins-api');
var usbConnector = require('./usbConnector');

var JOB_NAME = 'DevCI_iterative';
var SUCCESS_STATUS = 'SUCCESS';
var JENKINS_URL = 'http://epbygrow0110.grodno.epam.com:8080/jenkins/';

var jenkins = jenkinsapi.init(JENKINS_URL);

exports.checkStatus = function checkStatus(){
	jenkins.last_build_info(JOB_NAME, function(err, data){
 		console.log('Staus = ', data.result);
 		
 	 	if(data.result == SUCCESS_STATUS){
 	 		usbConnector.sendStatus(usbConnector.SUCCESS_STATUS);
 	 	}else{
 	 		usbConnector.sendStatus(usbConnector.FAIL_STATUS);
 	 	}
	});
}