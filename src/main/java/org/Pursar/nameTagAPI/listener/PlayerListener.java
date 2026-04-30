package org.Pursar.nameTagAPI.listener;

import org.Pursar.nameTagAPI.NameTagAPI;
import org.Pursar.nameTagAPI.manager.NameTagManager;
import org.Pursar.nameTagAPI.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class PlayerListener implements Listener {

    private final NameTagAPI plugin;
    private final NameTagManager nameTagManager;
    private final PlayerDataManager playerDataManager;

    public PlayerListener(NameTagAPI plugin) {
        this.plugin = plugin;
        this.nameTagManager = plugin.getNameTagManager();
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String tag = playerDataManager.getTag(player);
        if (!tag.equals(player.getName())) {
            nameTagManager.updateAll(player, tag);
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            String targetTag = playerDataManager.getTag(target);
            if (targetTag.equals(target.getName())) {
                continue;
            }

            nameTagManager.update(target, targetTag, player);
        }
    }

    @EventHandler
    private void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String label = event.getMessage();
        if (!label.startsWith("/")) {
            return;
        }

        String[] args = label.split(" ");
        if (args.length < 2) {
            return;
        }

        Map<String, String> tags = playerDataManager.getTags();
        for (String arg : args) {
            if (tags.containsKey(arg)) {
                String result = label
                        .replaceFirst("/", "")
                        .replaceFirst(arg, tags.get(arg));

                event.setCancelled(true);
                event.getPlayer().performCommand(result);
                return;
            }
        }
    }
}
