package org.rofu.eholygarant.core.model;

import java.util.UUID;

public class DealStatsBuilder {
    private UUID moderatorId;
    private int successCount;
    private int cancelledCount;
    private double totalEarned;

    DealStatsBuilder() {
    }

    public DealStatsBuilder moderatorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
        return this;
    }

    public DealStatsBuilder successCount(int successCount) {
        this.successCount = successCount;
        return this;
    }

    public DealStatsBuilder cancelledCount(int cancelledCount) {
        this.cancelledCount = cancelledCount;
        return this;
    }

    public DealStatsBuilder totalEarned(double totalEarned) {
        this.totalEarned = totalEarned;
        return this;
    }

    public DealStats build() {
        return new DealStats(this.moderatorId, this.successCount, this.cancelledCount, this.totalEarned);
    }

    public String toString() {
        return "DealStats.DealStatsBuilder(moderatorId=" + this.moderatorId + ", successCount=" + this.successCount + ", cancelledCount=" + this.cancelledCount + ", totalEarned=" + this.totalEarned + ")";
    }
}

