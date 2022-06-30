package ru.primer.mc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static ru.primer.mc.Main.database;
import static ru.primer.mc.Main.main;
import static ru.primer.mc.util.Utils.*;

public class CommandLogin implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration cfg = main.getConfig();
        if (!(sender instanceof Player)) {
            System.out.println(format(cfg.getString("not-player")));
            return true;
        }

        if (args.length != 1) {
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();

        try {
            if (database.getPassword(playerName) == null) {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String passwordInput = args[0];
        String passwordDB;
        try {
            passwordDB = database.getPassword(playerName);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        if (!passwordDB.equals(passwordInput)) {
            sendMessage(player, format(cfg.getString("incorrect-password")));
            return true;
        }

        sendMessage(player, cfg.getString("succes-login"));

        playersInAuthorization.remove(player);
        return true;
    }
}
