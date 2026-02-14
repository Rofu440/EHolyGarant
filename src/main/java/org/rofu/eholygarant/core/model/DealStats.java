package org.rofu.eholygarant.core.model;

import java.util.UUID;

public class DealStats {
    private UUID moderatorId;
    private int successCount;
    private int cancelledCount;
    private double totalEarned;

    public void addSuccess(double amount) {
        ++this.successCount;
        this.totalEarned += amount;
    }

    public void addCancelled() {
        ++this.cancelledCount;
    }

    public static DealStatsBuilder builder() {
        return new DealStatsBuilder();
    }

    public UUID getModeratorId() {
        return this.moderatorId;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    public int getCancelledCount() {
        return this.cancelledCount;
    }

    public double getTotalEarned() {
        return this.totalEarned;
    }

    public void setModeratorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public void setCancelledCount(int cancelledCount) {
        this.cancelledCount = cancelledCount;
    }

    public void setTotalEarned(double totalEarned) {
        this.totalEarned = totalEarned;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DealStats)) {
            return false;
        } else {
            DealStats other = (DealStats)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getSuccessCount() != other.getSuccessCount()) {
                return false;
            } else if (this.getCancelledCount() != other.getCancelledCount()) {
                return false;
            } else if (Double.compare(this.getTotalEarned(), other.getTotalEarned()) != 0) {
                return false;
            } else {
                Object this$moderatorId = this.getModeratorId();
                Object other$moderatorId = other.getModeratorId();
                if (this$moderatorId == null) {
                    if (other$moderatorId != null) {
                        return false;
                    }
                } else if (!this$moderatorId.equals(other$moderatorId)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof DealStats;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getSuccessCount();
        result = result * 59 + this.getCancelledCount();
        long $totalEarned = Double.doubleToLongBits(this.getTotalEarned());
        result = result * 59 + (int)($totalEarned >>> 32 ^ $totalEarned);
        Object $moderatorId = this.getModeratorId();
        result = result * 59 + ($moderatorId == null ? 43 : $moderatorId.hashCode());
        return result;
    }

    public String toString() {
        return "DealStats(moderatorId=" + this.getModeratorId() + ", successCount=" + this.getSuccessCount() + ", cancelledCount=" + this.getCancelledCount() + ", totalEarned=" + this.getTotalEarned() + ")";
    }

    public DealStats() {
    }

    public DealStats(UUID moderatorId, int successCount, int cancelledCount, double totalEarned) {
        this.moderatorId = moderatorId;
        this.successCount = successCount;
        this.cancelledCount = cancelledCount;
        this.totalEarned = totalEarned;
    }
}
