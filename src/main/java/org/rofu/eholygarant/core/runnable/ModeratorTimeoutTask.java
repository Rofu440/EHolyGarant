package org.rofu.eholygarant.core.runnable;

import java.util.Optional;
import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.deal.*;

public class ModeratorTimeoutTask extends BukkitRunnable {
    private final EHolyGarant plugin;
    private final UUID dealId;
    private int remainingSeconds;

    public ModeratorTimeoutTask(EHolyGarant plugin, UUID dealId, int seconds) {
        this.plugin = plugin;
        this.dealId = dealId;
        this.remainingSeconds = seconds;
    }

    public void run() {
        Optional<Deal> dealOpt = this.plugin.getDealManager().getDealById(this.dealId);
        if (dealOpt.isPresent() && ((Deal)dealOpt.get()).getStatus() == DealStatus.IN_PROGRESS) {
            --this.remainingSeconds;
            if (this.remainingSeconds <= 0) {
                this.plugin.getDealManager().handleModeratorTimeout(this.dealId);
                this.cancel();
            }

        } else {
            this.cancel();
        }
    }
}

