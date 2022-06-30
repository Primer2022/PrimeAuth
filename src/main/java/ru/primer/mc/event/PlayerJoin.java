package ru.primer.mc.event;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

import static ru.primer.mc.Main.database;
import static ru.primer.mc.Main.main;
import static ru.primer.mc.util.Utils.authScheduler;
import static ru.primer.mc.util.Utils.playersInAuthorization;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException, ClassNotFoundException {
        FileConfiguration cfg = main.getConfig();
        Player player = event.getPlayer();

        if(!playersInAuthorization.contains(player)) {
            playersInAuthorization.add(player);
        }

        if(database.getPassword(player.getName()) != null) {
            authScheduler(player, cfg.getStringList("auth-message"));
            return;
        }

        authScheduler(player, cfg.getStringList("register-message"));
    }
}
