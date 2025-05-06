package com.soranet.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.soranet.config.DbConfig;
import com.soranet.util.queries.DashboardQueries;

public class DashboardStatsService {
	// Total registered users
	public int getTotalUsers() throws SQLException, ClassNotFoundException {
		try (Connection conn = DbConfig.getDbConnection();
				PreparedStatement pstmt = conn.prepareStatement(DashboardQueries.TOTAL_USERS);
				ResultSet rs = pstmt.executeQuery()) {
			return rs.next() ? rs.getInt(1) : 0;
		}
	}
}
