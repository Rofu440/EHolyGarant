package org.rofu.eholygarant.core.runnable;

import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.deal.*;

public class DealTimeoutTask extends BukkitRunnable {
    private final EHolyGarant plugin;
    private final UUID dealId;
    private int remainingSeconds;

    public DealTimeoutTask(EHolyGarant plugin, UUID dealId, int seconds) {
        this.plugin = plugin;
        this.dealId = dealId;
        this.remainingSeconds = seconds;
    }

    public void run() {
        Optional<Deal> dealOpt = this.plugin.getDealManager().getDealById(this.dealId);
        if (dealOpt.isPresent() && ((Deal)dealOpt.get()).getStatus() == DealStatus.WAITING) {
            Deal deal = (Deal)dealOpt.get();
            --this.remainingSeconds;
            Player player = Bukkit.getPlayer(deal.getPlayerId());
            if (player != null && player.isOnline()) {
                this.plugin.getBossBarManager().showSearchBar(player, this.remainingSeconds);
            }

            if (this.remainingSeconds <= 0) {
                this.plugin.getDealManager().handleTimeout(this.dealId);
                this.cancel();
            }

        } else {
            this.cancel();
        }
    }
}

