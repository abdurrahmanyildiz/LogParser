/*******************************************************
 * Copyright (C) 2018 ABDURRAHMAN YILDIZ <abdurrahmanyildiz35@gmail.com>
 * 
 * This file is part of LogParser project.
 * 
 * All Rights Reserved
 * Lorem ipsum dolor sit amet. blah blah blah.
 * LogParser can not be copied and/or distributed without the express
 * permission of ABDURRAHMAN YILDIZ
 *******************************************************/

package com.ay.parser;

import java.util.HashMap;

import com.ay.parser.common.LogFileReader;
import com.ay.parser.common.Utils;
import com.ay.parser.data.MySqlDBUtils;
import com.ay.parser.models.ArgParameter;
import com.ay.parser.models.LogParserException;

/**
 * @author Abdurrahman
 *
 *         The main class
 */
public class LogParser {

	public static void main(String[] args) {
		try {

			ArgParameter argParams = Utils.GetArgParameters(args);

			HashMap<String, Integer> filteredList = Utils.FilterWithThreshold(
					LogFileReader.readLogFileWithParameters(argParams.getStartDate(), argParams.getEndDate()),
					argParams.getThreshold());
			if (filteredList.size()>0) {
				Utils.WriteConsoleByFilter(filteredList, argParams.getThreshold());

				MySqlDBUtils.InsertBlockedIpToDB(filteredList, Utils.BLOCK_COMMENT,
						Utils.SIMPLE_DATE_FORMATTER.format(argParams.getStartDate()),
						argParams.getDuration().parameterName(), argParams.getThreshold());
			} else {
				System.out.println(Utils.ERROR_EMTPTY_LIST);
			}
			

		} catch (LogParserException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
