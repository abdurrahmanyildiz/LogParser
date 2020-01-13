package com.ay.parser.data;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import com.ay.parser.common.Utils;
import com.ay.parser.models.LogParserException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * @author Abdurrahman
 *
 */
public class MySqlDBUtils {

	private static final String class_name = "com.mysql.jdbc.Connection";
	private static final String connection_string = "jdbc:mysql://localhost:3306/log_parser_db";
	private static final String user = "root";
	private static final String psw = "root";

	private static Connection ConnectToDB() throws LogParserException {
		try {
			Class.forName(class_name);
			Connection con = (Connection) DriverManager.getConnection(connection_string, user, psw);
			return con;
		} catch (Exception e) {
			throw new LogParserException(Utils.ERROR_SYSTEM_ERROR);
		}

	}

	private static void DisconnectToDB(Connection con) throws LogParserException {
		try {
			con.close();
		} catch (Exception e) {
			throw new LogParserException(Utils.ERROR_SYSTEM_ERROR);
		}
	}

	private static int ExecuteQuery(PreparedStatement statement) throws LogParserException {
		try {
			int executeUpdate = statement.executeUpdate();
			statement.close();

			return executeUpdate;
		} catch (Exception e) {
			throw new LogParserException(Utils.ERROR_SYSTEM_ERROR);
		}
	}

	public static void InsertBlockedIpToDB(HashMap<String, Integer> blockList, String comment, String startDateParam,
			String durationParam, Integer thresholdParam) throws LogParserException {
		Connection con = ConnectToDB();

		blockList.forEach((k, v) -> {
			try {
				ExecuteQuery(
						createInsertQueryToBlockIP(con, k, comment, startDateParam, durationParam, thresholdParam));
			} catch (LogParserException e) {
			}
		});
		System.out.print("Related IP or IPs inserted to Block List");
		DisconnectToDB(con);
	}

	private static PreparedStatement createInsertQueryToBlockIP(Connection con, String ip, String comment,
			String startDateParam, String durationParam, Integer thresholdParam) throws LogParserException {
		PreparedStatement ps = null;
		try {
			ps = (PreparedStatement) con.prepareStatement(
					"insert into log_parser_db.blocked_ip (ip,comment, startdate_param, duration_param, threshold_param) values(?,?,?,?,?)");
			ps.setString(1, ip);
			ps.setString(2, comment);
			ps.setString(3, startDateParam);
			ps.setString(4, durationParam);
			ps.setInt(5, thresholdParam);

		} catch (SQLException e) {
			throw new LogParserException(Utils.ERROR_SYSTEM_ERROR);
		}
		return ps;
	}

}
