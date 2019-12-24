package net.vaultmc.vaultcore.utils;

import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;

import java.sql.*;

public class ConnectionHandler {
    @Getter
    private Connection connection;

    private int currentRecursions = 0;

    public ConnectionHandler() {
        setupConnection();
    }

    public ConnectionHandler(Connection connection) {
        this.connection = connection;
    }

    private void setupConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/VaultMC_Data?useSSL=false&autoReconnect=true", "tadhg",
                    VaultCore.getInstance().getConfig().getString("mysql-password"));
        } catch (ClassNotFoundException | SQLException e) {
            VaultCore.getInstance().getLogger().severe("An error occurred while attempting to open connection to the database:");
            e.printStackTrace();
        }
    }

    private void checkAndUpdateConnection() throws SQLException {
        if (connection.isClosed()) {
            setupConnection();
        }
    }

    public ResultSet executeQueryStatement(String sql, Object... objects) throws SQLException {
        checkAndUpdateConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= objects.length; i++) {
                statement.setObject(i, objects[i - 1]);
            }
            currentRecursions = 0;
            return statement.executeQuery();
        } catch (SQLException e) {
            if (e.getMessage().contains("Broken pipe")) {
                currentRecursions++;
                if (currentRecursions >= 7) {
                    VaultCore.getInstance().getLogger().severe("Too many recursion (" + currentRecursions + "). Interrupting.");
                    throw new RuntimeException();
                }
                VaultCore.getInstance().getLogger().warning("MySQL connection is interrupted. Probably the connection timeout is reached. " +
                        "Attempted to open connection again, further operations might fail. Please consider restarting the server if more errors " +
                        "occurred.");
                setupConnection();
                return executeQueryStatement(sql, objects);
            }
            throw new RuntimeException();
        }
    }

    public int executeUpdateStatement(String sql, Object... objects) throws SQLException {
        checkAndUpdateConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= objects.length; i++) {
                statement.setObject(i, objects[i - 1]);
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("Broken pipe")) {
                currentRecursions++;
                if (currentRecursions >= 7) {
                    VaultCore.getInstance().getLogger().severe("Too many recursion (" + currentRecursions + "). Interrupting.");
                    throw new RuntimeException();
                }
                VaultCore.getInstance().getLogger().warning("MySQL connection is interrupted. Probably the connection timeout is reached. " +
                        "Attempted to open connection again, further operations might fail. Please consider restarting the server if more errors " +
                        "occurred.");
                setupConnection();
                return executeUpdateStatement(sql, objects);
            }
            throw new RuntimeException();
        }
    }

    public long executeLargeUpdateStatement(String sql, Object... objects) throws SQLException {
        checkAndUpdateConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= objects.length; i++) {
                statement.setObject(i, objects[i - 1]);
            }
            return statement.executeLargeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("Broken pipe")) {
                currentRecursions++;
                if (currentRecursions >= 7) {
                    VaultCore.getInstance().getLogger().severe("Too many recursion (" + currentRecursions + "). Interrupting.");
                    throw new RuntimeException();
                }
                VaultCore.getInstance().getLogger().warning("MySQL connection is interrupted. Probably the connection timeout is reached. " +
                        "Attempted to open connection again, further operations might fail. Please consider restarting the server if more errors " +
                        "occurred.");
                setupConnection();
                return executeLargeUpdateStatement(sql, objects);
            }
            throw new RuntimeException();
        }
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    public void close() throws SQLException {
        connection.close();
    }
}
