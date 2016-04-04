package com.github.galiaf47.semaphore;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.JobWithDetails;

public class JenkinsService {
	private static final String JOB_NAME = Config.getJenkinsJob();
	
	public static final String STATUS_SUCCESS = "Success";
	public static final String STATUS_FAILURE = "Failure";
	public static final String STATUS_PENDING = "Pending";
	
	private JenkinsServer jenkins;
	
	public void connect() throws URISyntaxException {
		jenkins = new JenkinsServer(new URI(Config.getJenkinsUrl()), Config.getJenkinsLogin(), Config.getJenkinsPassword());
	}
	
	public String getStatus() throws IOException {
		BuildResult result = null;
		
		JobWithDetails job = jenkins.getJob(JOB_NAME).details();
		result = job.getLastBuild().details().getResult();
		
		return getStatusFromResult(result);
	}
	
	private String getStatusFromResult(BuildResult result) {
		if (result == null) {
			return STATUS_PENDING;
		} else if (result == BuildResult.SUCCESS) {
			return STATUS_SUCCESS;
		} else if (result == BuildResult.FAILURE) {
			return STATUS_FAILURE;
		}
		
		return null;
	}
}
