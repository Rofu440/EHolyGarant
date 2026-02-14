package org.rofu.eholygarant.manager;

import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.core.model.DealStats;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StatsManager {
    private final EHolyGarant plugin;
    private final Map<UUID, DealStats> statsCache = new ConcurrentHashMap<>();

    public StatsManager(EHolyGarant plugin) {
        this.plugin = plugin;
    }

    public DealStats getStats(UUID moderatorId) {
        return statsCache.computeIfAbsent(moderatorId, id -> plugin.getStorage().getStats(id));
    }

    public void addSuccess(UUID moderatorId, double amount) {
        DealStats stats = getStats(moderatorId);
        stats.addSuccess(amount);
        saveStats(stats);
    }

    public void addCancelled(UUID moderatorId) {
        DealStats stats = getStats(moderatorId);
        stats.addCancelled();
        saveStats(stats);
    }

    private void saveStats(DealStats stats) {
        plugin.getStorage().saveStats(stats);
    }

    public void clearCache() {
        statsCache.clear();
    }
}