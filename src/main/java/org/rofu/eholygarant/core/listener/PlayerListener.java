package org.rofu.eholygarant.core.listener;

import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.deal.Deal;
import org.rofu.eholygarant.deal.DealStatus;

public class PlayerListener implements Listener {
    private final EHolyGarant plugin;

    public PlayerListener(EHolyGarant plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.plugin.getDealManager().handlePlayerQuit(player.getUniqueId());
        this.plugin.getBossBarManager().remove(player);
        this.plugin.getMenuManager().removeMenu(player.getUniqueId());
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Optional<Deal> playerDeal = this.plugin.getDealManager().getActiveDealByPlayer(player.getUniqueId());
        if (playerDeal.isPresent()) {
            Deal deal = (Deal)playerDeal.get();
            if (deal.getStatus() == DealStatus.WAITING) {
                this.plugin.getBossBarManager().showSearchBar(player, this.plugin.getConfigManager().getSearchTime());
            } else if (deal.getStatus() == DealStatus.IN_PROGRESS) {
                this.plugin.getBossBarManager().showDealBar(player);
            }
        }

        Optional<Deal> moderatorDeal = this.plugin.getDealManager().getActiveDealByModerator(player.getUniqueId());
        if (moderatorDeal.isPresent()) {
            Deal deal = (Deal)moderatorDeal.get();
            if (deal.getStatus() == DealStatus.IN_PROGRESS) {
                this.plugin.getBossBarManager().showModeratorBar(player);
            }
        }

    }
}

