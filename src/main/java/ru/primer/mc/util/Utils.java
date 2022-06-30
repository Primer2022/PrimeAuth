package ru.primer.mc.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static ru.primer.mc.Main.main;

public class Utils {

    public static List<Player> playersInAuthorization= new ArrayList<>();

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(format(msg));
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        player.sendTitle(format(title), format(subTitle), 20, 20, 20);
    }

    public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(format(title), format(subTitle), fadeIn, stay, fadeOut);
    }

    public static void sendActionBar(Player player, String msg) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(format(msg)));
    }


    public static void authScheduler(Player player, List<String> list) {
        FileConfiguration cfg = main.getConfig();
        int delay = cfg.getInt("message-delay");

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(!playersInAuthorization.contains(player)) {
                    this.cancel();
                }
                list.forEach(message -> sendMessage(player, format(message)));
                ;
            }
        };
        bukkitRunnable.runTaskTimer(main, 0, delay * 20L);
    }
}
