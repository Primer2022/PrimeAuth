package ru.primer.mc;

import org.bukkit.plugin.java.JavaPlugin;
import ru.primer.mc.command.CommandLogin;
import ru.primer.mc.command.CommandReg;
import ru.primer.mc.database.Database;
import ru.primer.mc.event.PlayerJoin;
import ru.primer.mc.event.PlayerMove;

import java.sql.SQLException;

public final class Main extends JavaPlugin {

    public static Database database = new Database();

    private final String HOST = "localhost";
    private final int PORT = 3306;
    private final String DB_NAME = "name";
    private final String LOGIN = "login";
    private final String PASS = "password";

    public static Main main;

    @Override
    public void onEnable() {
        try {
            database.MySQLDatabase(HOST, PORT, DB_NAME, LOGIN, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        saveDefaultConfig();

        getCommand("register").setExecutor(new CommandReg());
        getCommand("login").setExecutor(new CommandLogin());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);

        main = this;
    }

    @Override
    public void onDisable() {

    }
}
