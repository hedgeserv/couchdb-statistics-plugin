package org.jenkinsci.plugins.couchstats;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import hudson.scm.SCM;
import hudson.util.CopyOnWriteMap;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Send jenkins result and duration of Jenkins jobs to a couchdb/cloudant server
 */
@Extension
public class CouchStatsListener extends RunListener<Run> {

	private static final Logger LOGGER = Logger.getLogger(CouchStatsListener.class.getName());

	@Override
	public void onCompleted(final Run r, final TaskListener listener) {
		CouchStatsConfig config = CouchStatsConfig.get();

		if (config.getUrl() == null || config.getUrl() == "") {
			LOGGER.log(Level.WARNING, "CouchStats plugin not configured, skipping");
			return;
		}

		String jobName = r.getParent().getFullName().toString();
		String result = r.getResult().toString();
		long duration = r.getDuration();
		long timeInMillis = r.getTimeInMillis();
		String timeString = r.getTimestampString();
		String timeStamp = TimeUtils.timeStamp(timeInMillis);
		int buildId = r.getNumber();
		String buildUrl = r.getUrl();
		String buildFullUrl = r.getAbsoluteUrl();



		AbstractBuild build = (AbstractBuild)r.getParent().getBuildByNumber(buildId);
		SCM scm = build.getProject().getScm();

		String scmType = scm.getType();

		Map<String, String> scmValues = new CopyOnWriteMap.Hash<String, String>();
		scm.buildEnvVars(build, scmValues);


		LOGGER.log(Level.INFO, "CouchStatsListener: config: " + config);
		LOGGER.log(Level.INFO, "CouchStatsListener: job: " + jobName + ", result: " + result + ", duration: "
				+ duration);

		try {
			LOGGER.log(Level.INFO, "Sending stats to " + config.getUrl());
			HttpClient client = new StdHttpClient.Builder().url(config.getUrl()).username(config.getUsername())
					.password(config.getPassword()).build();

			CouchDbInstance instance = new StdCouchDbInstance(client);
			CouchDbConnector connector = new StdCouchDbConnector(config.getDocument(), instance);

			StatsRecord record = new StatsRecord();
			record.setJobName(jobName);
			record.setStatus(result);
			record.setDuration(duration);
			record.setTimeInMillis(timeInMillis);
			record.setTimeString(timeString);
			record.setTimeStamp(timeStamp);
			record.setBuildId(buildId);
			record.setBuildURL(buildUrl);
			record.setScmType(scmType);
			record.setBuildEnvVars(scmValues);
			record.setBuildFullUrl(buildFullUrl);


			LOGGER.log(Level.FINE, "Saving build record...");
			StatsRecordRepository repository = new StatsRecordRepository(connector);
			repository.add(record);
			LOGGER.log(Level.FINE, "Saving build record. Done");
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, "Unable to configure couchdb connector");
		}
	}

}
