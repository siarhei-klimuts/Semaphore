package com.github.galiaf47.semaphore;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.JobWithDetails;

public class JenkinsService {
	private JenkinsServer jenkins;
	
	public void connect() throws URISyntaxException {
		jenkins = new JenkinsServer(new URI(Config.JENKINS_URL), Config.JENKINS_LOGIN, Config.JENKINS_PASSWORD);
	}
	
	public void disconnect() {
		jenkins = null;
	}
	
	public String getStatus() throws IOException {
		BuildResult result = null;
		
		JobWithDetails job = jenkins.getJob(Config.JENKINS_JOB).details();
		result = job.getLastBuild().details().getResult();
		
		return getStatusFromResult(result);
	}
	
	private String getStatusFromResult(BuildResult result) {
		if (result == null) {
			return Config.JENKINS_STATUS_PENDING;
		} else if (result == BuildResult.SUCCESS) {
			return Config.JENKINS_STATUS_SUCCESS;
		} else if (result == BuildResult.FAILURE) {
			return Config.JENKINS_STATUS_FAILURE;
		}
		
		return null;
	}
}
