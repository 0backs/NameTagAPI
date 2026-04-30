package org.Pursar.nameTagAPI;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.Pursar.nameTagAPI.listener.PlayerListener;
import org.Pursar.nameTagAPI.manager.NameTagManager;
import org.Pursar.nameTagAPI.manager.PlayerDataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NameTagAPI extends JavaPlugin {

    private static NameTagAPI instance;

    private ProtocolManager protocolManager;
    private NameTagManager nameTagManager;
    private PlayerDataManager playerDataManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        if (!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("ProtocolLib 플러그인이 인식되지 않습니다. 플러그인이 비활성화됩니다.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        nameTagManager = new NameTagManager(this);
        playerDataManager = new PlayerDataManager(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        playerDataManager.loadData();
        getLogger().info("플러그인이 활성화 되었습니다.");
    }

    @Override
    public void onDisable() {
        playerDataManager.saveData();
        getLogger().info("플러그인이 비활성화 되었습니다.");
    }

    public static NameTagAPI getInstance() {
        return instance;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public NameTagManager getNameTagManager() {
        return nameTagManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
