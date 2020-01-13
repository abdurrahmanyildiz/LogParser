package com.ay.parser.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.ay.parser.models.ArgParameter;
import com.ay.parser.enums.Duration;
import com.ay.parser.models.LogParserException;

/**
 * @author Abdurrahman
 *
 */
public class Utils {
	public static final String LOG_FILE_PATH_PARAM_NAME = "--accesslog";
	public static final String START_DATE_PARAM_NAME = "--startDate";
	public static final String THRESHOLD_PARAM_NAME = "--threshold";
	public static final String DURATION_PARAM_NAME = "--duration";
	public static final String SEPERATOR = "\\|";
	public static final String LOG_FILE_PATH = "access.log";
	public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
	public static final SimpleDateFormat SIMPLE_DATE_FORMATTER_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static final Pattern ARG_PARAMS_PATTERN = Pattern.compile("^[-]{0,2}(\\w+)[=](.*)$");
	public static final String BLOCK_COMMENT = "Because of too many connection request of this ip log parser app blocked this ip.";

	public static final String ERROR_EMTPTY_LIST = "Any IP can not detected with the related parameters.";
	public static final String ERROR_SYSTEM_ERROR = "An error occured while system running. Please try again.";
	public static final String ERROR_FILE_PATH = "The accesslog parameter or path is invalid. Please Specify file path and parameter as: --accesslog=/path/to/file";
	public static final String ERROR_START_DATE = "The startDate parameter is invalid. The format is yyyy-MM-dd.HH:mm:ss, e.g. 2017-01-01.13:00:00.";
	public static final String ERROR_DURATION = "The duration parameter is invalid. The format is --duration=hourly (only hourly or daily)";
	public static final String ERROR_THRESHOLD = "The threshold parameter is invalid. The format is --threshold=100 (only a positive number)";

	public static Duration parseDurationToEnum(String enumString) throws LogParserException {
		if (!isNullOrEmpty(enumString)) {
			if (enumString.equals(Duration.Hourly.parameterName())) {
				return Duration.Hourly;
			} else if (enumString.equals(Duration.Daily.parameterName())) {
				return Duration.Daily;
			} else {
				throw new LogParserException(ERROR_DURATION);
			}
		}
		return null;
	}

	/*
	 * Checks if a string is null or empty. returns true if string null or empty by
	 * abdurrahmanY
	 */
	public static boolean isNullOrEmpty(String input) {
		return input == null || input.isEmpty();
	}

	public static HashMap<String, Integer> FilterWithThreshold(HashMap<String, Integer> map, Integer threshold) {
		HashMap<String, Integer> filteredIp = new HashMap<String, Integer>();
		map.forEach((key, value) -> {
			if (value >= threshold) {
				filteredIp.put(key, value);
			}
		});

		return filteredIp;
	}

	public static void WriteConsoleByFilter(HashMap<String, Integer> map, Integer filter) {
		map.forEach((key, value) -> {
			if (value >= filter) {
				System.out.println("#ip: " + key + " # ping count: " + value);
			}
		});
	}

	public static ArgParameter GetArgParameters(String[] args) throws LogParserException {
		ArgParameter argParam = new ArgParameter();

		// *****access.log file path parameter is setting
		String param = Arrays.asList(args).stream().filter(s -> s.startsWith(Utils.LOG_FILE_PATH_PARAM_NAME))
				.findFirst().orElse(null);
		if (param != null) {
			argParam.setLogFilePath(param.split("=")[1]);
		} else {
			throw new LogParserException(ERROR_FILE_PATH);
		}
		// *****startDate parameter is setting
		param = Arrays.asList(args).stream().filter(s -> s.startsWith(START_DATE_PARAM_NAME)).findFirst().orElse(null);

		if (param != null) {
			try {
				argParam.setStartDate(SIMPLE_DATE_FORMATTER.parse(param.split("=")[1]));
			} catch (ParseException e) {
				new LogParserException("The startdate parameter is in invalid format" + e.getMessage());
			}
		} else {
			throw new LogParserException(ERROR_START_DATE);
		}
		// ***** duration parameter is setting
		param = Arrays.asList(args).stream().filter(s -> s.startsWith(DURATION_PARAM_NAME)).findFirst().orElse(null);
		if (param != null) {
			argParam.setDuration(parseDurationToEnum(param.split("=")[1]));
		} else {
			throw new LogParserException(ERROR_DURATION);
		}
		// ***** threshold parameter is setting
		param = Arrays.asList(args).stream().filter(s -> s.startsWith(THRESHOLD_PARAM_NAME)).findFirst().orElse(null);
		if (param != null) {
			int threshold = Integer.parseInt(param.split("=")[1]);
			if (threshold <= 0) {
				throw new LogParserException(ERROR_THRESHOLD);
			} else {
				argParam.setThreshold(threshold);
			}
		} else {
			throw new LogParserException(ERROR_THRESHOLD);
		}

		return argParam;
	}
}
