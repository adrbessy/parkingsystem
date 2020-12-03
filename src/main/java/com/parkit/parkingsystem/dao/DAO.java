package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;

public class DAO {

	public static final Logger logger = LogManager.getLogger("DAO");
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();
	public Connection con = null;
	PreparedStatement ps = null;

	public void setUpDB(String dbConstants) {
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(dbConstants);
		} catch (Exception ex) {
			logger.error("Error instantiating database table: " + ex.getMessage());
		}
	}

	public void tearDown() {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				logger.error("The request cannot be closed", e);
			}
		}
		dataBaseConfig.closeConnection(con);
	}

}
