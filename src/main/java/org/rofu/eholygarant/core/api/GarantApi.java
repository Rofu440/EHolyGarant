package org.rofu.eholygarant.core.api;

import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.deal.Deal;
import org.rofu.eholygarant.deal.DealStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface GarantApi {

    static GarantApi getInstance() {
        return EHolyGarant.getInstance().getDealManager();
    }

    Optional<Deal> createDeal(UUID var1, String var2);

    boolean acceptDeal(UUID var1, UUID var2);

    boolean completeDeal(UUID var1);

    boolean cancelDeal(UUID var1, String var2);

    Optional<Deal> getDealById(UUID var1);

    Optional<Deal> getActiveDealByPlayer(UUID var1);

    Optional<Deal> getActiveDealByModerator(UUID var1);

    List<Deal> getAllActiveDeals();

    List<Deal> getDealsByStatus(DealStatus var1);

    boolean hasActiveDeal(UUID var1);

    boolean isModerating(UUID var1);
}

