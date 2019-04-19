package com.lewisallen.rtdptiCache.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;


public class TransportDatabase
{
    private Statement stmt;

    /**
     * Executes the provided query on the database.
     * Credentials specified in the .env file at the project root.
     *
     * @param query The query to execute.
     * @return Results of query.
     * @throws SQLException
     */
    public ResultSet query(String query, Connection conn) throws SQLException
    {
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public Connection getDbConnection() throws SQLException, ClassNotFoundException {
        Dotenv env = Dotenv.load();
        Class.forName("com.mysql.cj.jdbc.Driver");

        String connectionString = String.format("jdbc:mysql://%s:%s/%s", env.get("DB_HOST"), env.get("DB_PORT"), env.get("DB_NAME"));
        Connection conn = DriverManager.getConnection(connectionString, env.get("DB_USERNAME"), env.get("DB_PASSWORD"));
        return conn;
    }
}
