package org.rofu.eholygarant.deal;

import java.time.LocalDateTime;
import java.util.UUID;

public class DealBuilder {
    private UUID id;
    private UUID playerId;
    private String playerName;
    private String description;
    private LocalDateTime createdAt;
    private double price;
    private UUID moderatorId;
    private String moderatorName;
    private LocalDateTime acceptedAt;
    private DealStatus status;

    DealBuilder() {
    }

    public DealBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public DealBuilder playerId(UUID playerId) {
        this.playerId = playerId;
        return this;
    }

    public DealBuilder playerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public DealBuilder description(String description) {
        this.description = description;
        return this;
    }

    public DealBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public DealBuilder price(double price) {
        this.price = price;
        return this;
    }

    public DealBuilder moderatorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
        return this;
    }

    public DealBuilder moderatorName(String moderatorName) {
        this.moderatorName = moderatorName;
        return this;
    }

    public DealBuilder acceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
        return this;
    }

    public DealBuilder status(DealStatus status) {
        this.status = status;
        return this;
    }

    public Deal build() {
        return new Deal(this.id, this.playerId, this.playerName, this.description, this.createdAt, this.price, this.moderatorId, this.moderatorName, this.acceptedAt, this.status);
    }

    public String toString() {
        return "Deal.DealBuilder(id=" + this.id + ", playerId=" + this.playerId + ", playerName=" + this.playerName + ", description=" + this.description + ", createdAt=" + this.createdAt + ", price=" + this.price + ", moderatorId=" + this.moderatorId + ", moderatorName=" + this.moderatorName + ", acceptedAt=" + this.acceptedAt + ", status=" + this.status + ")";
    }
}
