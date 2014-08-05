var jenkins = require('./jenkins');

var schedule = require('node-schedule');
var rule = new schedule.RecurrenceRule();
rule.Second = 1;

var j = schedule.scheduleJob(rule, function(){
	jenkins.checkStatus();
});