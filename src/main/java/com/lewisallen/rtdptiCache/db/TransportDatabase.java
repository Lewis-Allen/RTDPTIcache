package com.lewisallen.rtdptiCache.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;


public class TransportDatabase
{

    /**
     * Executes the provided query on the database.
     * Credentials specified in the .env file at the project root.
     *
     * @param query The query to execute.
     * @return Results of query.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ResultSet query(String query) throws SQLException, ClassNotFoundException
    {
        Statement stmt = getDbStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    /**
     * Generates the interface for a SQL statement.
     *
     * @return Interface for SQL statement.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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
