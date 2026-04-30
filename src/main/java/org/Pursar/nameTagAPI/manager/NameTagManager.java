package org.Pursar.nameTagAPI.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedRemoteChatSessionData;
import org.Pursar.nameTagAPI.NameTagAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class NameTagManager {

    /**
     * reference : https://stackoverflow.com/questions/78600407/how-can-i-modify-a-players-name-tag-using-protocollib-or-in-bukkit-to-achieve-a
     * reference : https://github.com/dmulloy2/ProtocolLib/issues/3328
     */

    private final NameTagAPI plugin;
    private final ProtocolManager protocolManager;

    public NameTagManager(NameTagAPI plugin) {
        this.plugin = plugin;
        this.protocolManager = plugin.getProtocolManager();
    }

    public void update(Player player, String tag, Player target) {
        applyTag(player, tag, target);
    }

    public void updateAll(Player player, String tag) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            applyTag(player, tag, target);
        }
    }

    private void applyTag(Player player, String tag, Player target) {
        boolean equal = player.equals(target);
        if (!equal) {
            target.hidePlayer(plugin, player);
        }

        PacketContainer removePacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        removePacket.getUUIDLists().write(0, Collections.singletonList(player.getUniqueId()));
        protocolManager.sendServerPacket(target, removePacket);

        WrappedGameProfile oldProfile = WrappedGameProfile.fromPlayer(player);

        ArrayList<PlayerInfoData> datas = new ArrayList<>();
        datas.add(new PlayerInfoData(
                new WrappedGameProfile(oldProfile.getUUID(), tag),
                player.getPing(),
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                null
        ));

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0, EnumSet.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED
        ));
        packet.getPlayerInfoDataLists().write(1, datas);

        protocolManager.sendServerPacket(target, packet);

        if (!equal) {
            target.showPlayer(plugin, player);
        }
    }
}
