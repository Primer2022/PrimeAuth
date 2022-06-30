package ru.primer.mc.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static ru.primer.mc.util.Utils.playersInAuthorization;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(!playersInAuthorization.contains(player)) {
            return;
        }

        event.setCancelled(true);
    }
}
