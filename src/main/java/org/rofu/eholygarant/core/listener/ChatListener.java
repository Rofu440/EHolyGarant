package org.rofu.eholygarant.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.core.model.PlayerState;

public class ChatListener implements Listener {
    private final EHolyGarant plugin;

    public ChatListener(EHolyGarant plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerState state = plugin.getDealManager().getPlayerState(player.getUniqueId());
        if (state == PlayerState.NONE) return;

        event.setCancelled(true);
        String message = event.getMessage();

        plugin.getServer().getScheduler().runTask(plugin, () -> state.handle(player, message, plugin));
    }
}