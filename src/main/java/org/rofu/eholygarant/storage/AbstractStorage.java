package org.rofu.eholygarant.storage;

import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.deal.Deal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.rofu.eholygarant.deal.*;
import org.rofu.eholygarant.core.model.DealStats;

public abstract class AbstractStorage implements Storage {
    protected static final DateTimeFormatter FORMATTER;
    protected final EHolyGarant plugin;

    protected Deal buildDeal(UUID id, UUID playerId, String playerName, String description, String createdAt, double price, String status, String moderatorId, String moderatorName, String acceptedAt) {
        DealBuilder builder = Deal.builder().id(id).playerId(playerId).playerName(playerName).description(description).createdAt(LocalDateTime.parse(createdAt, FORMATTER)).price(price).status(DealStatus.valueOf(status));
        if (moderatorId != null && !moderatorId.isEmpty()) {
            builder.moderatorId(UUID.fromString(moderatorId)).moderatorName(moderatorName);
            if (acceptedAt != null && !acceptedAt.isEmpty()) {
                builder.acceptedAt(LocalDateTime.parse(acceptedAt, FORMATTER));
            }
        }

        return builder.build();
    }

    protected DealStats buildStats(UUID moderatorId, int success, int cancelled, double earned) {
        return DealStats.builder().moderatorId(moderatorId).successCount(success).cancelledCount(cancelled).totalEarned(earned).build();
    }

    public AbstractStorage(EHolyGarant plugin) {
        this.plugin = plugin;
    }

    static {
        FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }
}

