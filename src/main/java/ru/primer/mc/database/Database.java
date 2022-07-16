package ru.primer.mc.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.scheduler.BukkitRunnable;
import ru.primer.mc.Main;

import java.sql.*;

public class Database {

    private HikariDataSource src;

    public void MySQLDatabase(String host, int port, String database, String user, String password) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        src = new HikariDataSource(config);

        try (Connection cn = connect(); Statement st = cn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS `users` (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), password VARCHAR(32), PRIMARY KEY(id))");
        }
    }

    private Connection connect() throws SQLException {
        return src.getConnection();
    }

    public void insertPassword(String name, String password) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `users` (name, password) VALUES (?, ?)";
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    createTable();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                PreparedStatement prSt = null;
                try {
                    prSt = connect().prepareStatement(sql);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    prSt.setString(1, name);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    prSt.setString(2, password);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    prSt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        bukkitRunnable.runTaskAsynchronously(Main.main);
    }

    public String getPassword(String playerName) throws SQLException, ClassNotFoundException {
        final String[] password = {null};
        String sql = "SELECT `password` FROM `users` WHERE `name` = '" + playerName + "'";
        long time = System.currentTimeMillis();
        System.out.println(time);
        createTable();
        PreparedStatement prSt = connect().prepareStatement(sql);

        Statement statement = null;
        statement = connect().createStatement();
        ResultSet res = null;
        res = statement.executeQuery(sql);

        while (true) {
            if (!res.next()) break;
            password[0] = res.getString(1);
        }

        statement.close();
        System.out.println(System.currentTimeMillis() - time);
        return password[0];
    }

    private void createTable() throws SQLException, ClassNotFoundException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `users` (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(30), password VARCHAR(32), PRIMARY KEY(id))";

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                Statement stmt = null;
                try {
                    stmt = connect().createStatement();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    stmt.execute(sqlCreate);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        bukkitRunnable.runTaskAsynchronously(Main.main);
    }

}
