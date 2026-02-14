package org.rofu.eholygarant.storage;

import org.rofu.eholygarant.deal.Deal;
import org.rofu.eholygarant.core.model.DealStats;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Storage {
    void init();

    void shutdown();

    void saveDeal(Deal var1);

    Optional<Deal> getDeal(UUID var1);

    List<Deal> getActiveDeals();

    List<Deal> getDealsByPlayer(UUID var1);

    List<Deal> getDealsByModerator(UUID var1);

    void removeDeal(UUID var1);

    DealStats getStats(UUID var1);

    void saveStats(DealStats var1);

    void load();

    void save();
}
