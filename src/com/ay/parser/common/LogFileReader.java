package com.ay.parser.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

public class LogFileReader {

	public static HashMap<String, Integer> readLogFileWithParameters(Date startDate, Date endDate) {
		try {
			FileInputStream fstream = new FileInputStream("access.log");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			HashMap<String, Integer> filteredIpList = new HashMap<String, Integer>();

			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] partOfLine = strLine.split(Utils.SEPERATOR);
				if (partOfLine != null && partOfLine.length > 0) {
					Date pingDate = Utils.SIMPLE_DATE_FORMATTER_SSS.parse(partOfLine[0]);
					String ip = partOfLine[1];

					if (IPV4Checker.validate(ip) && pingDate.after(startDate) && pingDate.before(endDate)) {
						if (filteredIpList.containsKey(ip)) {
							filteredIpList.put(ip, filteredIpList.get(ip) + 1);
						} else {
							filteredIpList.put(ip, 1);
						}
					}
				}
			}
			fstream.close();
			return filteredIpList;
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return null;
	}

}
