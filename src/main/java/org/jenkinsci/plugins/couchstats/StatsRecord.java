package org.jenkinsci.plugins.couchstats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties({"_id", "_rev"})
public class StatsRecord {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("_rev")
    private String rev;
    private String jobName;
    private String status;
    private long duration;
    private long timeInMillis;
    private String timeString;
    private String timeStamp;
    private Map<String, String> buildEnvVars;
    private String scmType;
    private String buildFullUrl;

    public String getBuildFullUrl() {
        return buildFullUrl;
    }

    public void setBuildFullUrl(String buildFullUrl) {
        this.buildFullUrl = buildFullUrl;
    }

    public String getScmType() {
        return scmType;
    }

    public void setScmType(String scmType) {
        this.scmType = scmType;
    }


    public Map<String, String> getBuildEnvVars() {
        return buildEnvVars;
    }

    public void setBuildEnvVars(Map<String, String> buildEnvVars) {
        this.buildEnvVars = buildEnvVars;
    }


    public int getBuildId() {
        return buildId;
    }

    public void setBuildId(int buildId) {
        this.buildId = buildId;
    }

    private int buildId;

    public String getBuildURL() {
        return buildURL;
    }

    public void setBuildURL(String buildURL) {
        this.buildURL = buildURL;
    }

    private String buildURL;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
