package com.ay.parser.models;

import java.util.Date;

import com.ay.parser.enums.Duration;

/**
 * @author Abdurrahman
 *
 */
public class ArgParameter {

	private String logFilePath;

	private Date startDate;

	private Duration duration;

	private Integer threshold;

	private Date endDate;

	public ArgParameter() {

	}

	public ArgParameter(String logFilePath, Date startDate, Duration duration, Integer threshold) {
		super();
		this.logFilePath = logFilePath;
		this.startDate = startDate;
		this.duration = duration;
		this.threshold = threshold;
	}

	public Date getEndDate() {
		if (endDate == null) {
			endDate = duration.addToOriginalDate(getStartDate());
		}
		return endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Duration getDuration() {
		return duration;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
