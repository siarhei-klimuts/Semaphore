var jenkins = require('./jenkins');

setInterval(function () {
	jenkins.checkStatus();
}, 1000);