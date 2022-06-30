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

public class CommandReg implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        FileConfiguration cfg = main.getConfig();
        if(!(sender instanceof Player)) {
            System.out.println(format(cfg.getString("not-player")));
            return true;
        }

        if(args.length != 2) {
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();
        String password = args[0];
        String passwordRepeat = args[1];

        try {
            if(database.getPassword(playerName) != null) {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(!password.equals(passwordRepeat)) {
            sendMessage(player, format(cfg.getString("not-equals")));
            return true;
        }

        if(password.length() < 6) {
            sendMessage(player, format(cfg.getString("low-password-leght")));
            return true;
        }

        if(password.length() > 64) {
            sendMessage(player, format(cfg.getString("high-password-leght")));
            return true;
        }

        try {
            database.insertPassword(playerName, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        sendMessage(player, cfg.getString("succes-register"));

        playersInAuthorization.remove(player);
        return true;
    }
}
