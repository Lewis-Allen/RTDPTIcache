package com.lewisallen.rtdptiCache.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;


public class TransportDatabase {
	
	private String naptanQuery = "SELECT SystemCodeNumber, LongDescription, Identifier FROM naptan WHERE Active = 'True' AND Retrieve = 1;";
	private String stationQuery = "SELECT StationName, CRSCode FROM stations WHERE Retrieve = 1;";

	public ResultSet queryNaptan() throws SQLException, ClassNotFoundException
	{
		Statement stmt = getDbStatement();
		ResultSet rs = stmt.executeQuery(this.naptanQuery);
		return rs;
	}

	public ResultSet queryStation() throws SQLException, ClassNotFoundException
	{
		Statement stmt = getDbStatement();
		ResultSet rs = stmt.executeQuery(this.stationQuery);
		return rs;
	}

	public Statement getDbStatement() throws SQLException, ClassNotFoundException
	{
		Dotenv env = Dotenv.load();
		Class.forName("com.mysql.cj.jdbc.Driver");

		String connectionString = String.format("jdbc:mysql://%s:%s/%s", env.get("DB_HOST"), env.get("DB_PORT"), env.get("DB_NAME"));
		Connection conn = DriverManager.getConnection(connectionString, env.get("DB_USERNAME"), env.get("DB_PASSWORD"));
		Statement stmt = conn.createStatement();
		return stmt;
	}
}
