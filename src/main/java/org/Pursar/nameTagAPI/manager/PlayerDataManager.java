package org.Pursar.nameTagAPI.manager;

import org.Pursar.nameTagAPI.NameTagAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private final NameTagAPI plugin;
    private final NameTagManager nameTagManager;
    private final File dataFolder;
    private final Map<UUID, String> tagMap;

    public PlayerDataManager(NameTagAPI plugin) {
        this.plugin = plugin;
        this.nameTagManager = plugin.getNameTagManager();
        this.dataFolder = new File(plugin.getDataFolder(), "tagdata");
        this.tagMap = new ConcurrentHashMap<>();

        dataFolder.mkdirs();
    }

    public void loadData() {
        File dataFile = new File(dataFolder, "player.yml");
        if (!dataFile.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection sec = config.getConfigurationSection("list");
        if (sec == null) {
            return;
        }

        for (String uuidStr : sec.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            String tag = config.getString("list." + uuidStr);
            tagMap.put(uuid, tag);
        }
    }

    public void saveData() {
        File dataFile = new File(dataFolder, "player.yml");
        YamlConfiguration config = new YamlConfiguration();

        tagMap.forEach((uuid, tag) -> {
            config.set("list." + uuid, tag);
        });

        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("데이터를 저장하던 중, 오류가 발생했습니다. :");
            e.printStackTrace();
        }
    }

    public String getTag(Player player) {
        return tagMap.getOrDefault(player.getUniqueId(), player.getName());
    }

    public void setTag(Player player, String tag) {
        tagMap.put(player.getUniqueId(), tag);
        nameTagManager.updateAll(player, tag);
    }

    public Map<String, String> getTags() {
        Map<String, String> tags = new HashMap<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            String tag = getTag(player);
            if (tag.equals(name)) {
                continue;
            }

            tags.put(tag, name);
        }

        return tags;
    }
}
