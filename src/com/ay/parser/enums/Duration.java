package com.ay.parser.enums;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Abdurrahman
 *
 */
public enum Duration {
	Hourly("hourly", 1, Calendar.HOUR), Daily("daily", 1, Calendar.DATE);

	private String parameterName;
	private Integer amount;
	private Integer field;

	Duration(String parameterName, Integer amount, Integer field) {
		this.parameterName = parameterName;
		this.amount = amount;
		this.field = field;
	}

	public Date addToOriginalDate(Date startDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(field, amount);
		return cal.getTime();
	}

	public String parameterName() {
		return parameterName;
	}

}
