package org.rofu.eholygarant.deal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Deal {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final UUID id;
    private final UUID playerId;
    private final String playerName;
    private final String description;
    private final LocalDateTime createdAt;
    private final double price;
    private UUID moderatorId;
    private String moderatorName;
    private LocalDateTime acceptedAt;
    private DealStatus status;

    public static Deal create(UUID playerId, String playerName, String description, double price) {
        return builder().id(UUID.randomUUID()).playerId(playerId).playerName(playerName).description(description).createdAt(LocalDateTime.now()).price(price).status(DealStatus.WAITING).build();
    }

    public String getFormattedCreatedAt() {
        return this.createdAt.format(FORMATTER);
    }

    public String getFormattedAcceptedAt() {
        return this.acceptedAt != null ? this.acceptedAt.format(FORMATTER) : "N/A";
    }

    public void accept(UUID moderatorId, String moderatorName) {
        this.moderatorId = moderatorId;
        this.moderatorName = moderatorName;
        this.acceptedAt = LocalDateTime.now();
        this.status = DealStatus.IN_PROGRESS;
    }

    public void complete() {
        this.status = DealStatus.COMPLETED;
    }

    public void cancel() {
        this.status = DealStatus.CANCELLED;
    }

    public void timeout() {
        this.status = DealStatus.TIMEOUT;
    }

    public boolean isOwnedBy(UUID uuid) {
        return this.playerId.equals(uuid);
    }

    public boolean isModeratedBy(UUID uuid) {
        return this.moderatorId != null && this.moderatorId.equals(uuid);
    }

    Deal(UUID id, UUID playerId, String playerName, String description, LocalDateTime createdAt, double price, UUID moderatorId, String moderatorName, LocalDateTime acceptedAt, DealStatus status) {
        this.id = id;
        this.playerId = playerId;
        this.playerName = playerName;
        this.description = description;
        this.createdAt = createdAt;
        this.price = price;
        this.moderatorId = moderatorId;
        this.moderatorName = moderatorName;
        this.acceptedAt = acceptedAt;
        this.status = status;
    }

    public static DealBuilder builder() {
        return new DealBuilder();
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public double getPrice() {
        return this.price;
    }

    public UUID getModeratorId() {
        return this.moderatorId;
    }

    public String getModeratorName() {
        return this.moderatorName;
    }

    public LocalDateTime getAcceptedAt() {
        return this.acceptedAt;
    }

    public DealStatus getStatus() {
        return this.status;
    }

    public void setModeratorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
    }

    public void setModeratorName(String moderatorName) {
        this.moderatorName = moderatorName;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public void setStatus(DealStatus status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Deal)) {
            return false;
        } else {
            Deal other = (Deal)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (Double.compare(this.getPrice(), other.getPrice()) != 0) {
                return false;
            } else {
                label121: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label121;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label121;
                    }

                    return false;
                }

                Object this$playerId = this.getPlayerId();
                Object other$playerId = other.getPlayerId();
                if (this$playerId == null) {
                    if (other$playerId != null) {
                        return false;
                    }
                } else if (!this$playerId.equals(other$playerId)) {
                    return false;
                }

                label107: {
                    Object this$playerName = this.getPlayerName();
                    Object other$playerName = other.getPlayerName();
                    if (this$playerName == null) {
                        if (other$playerName == null) {
                            break label107;
                        }
                    } else if (this$playerName.equals(other$playerName)) {
                        break label107;
                    }

                    return false;
                }

                Object this$description = this.getDescription();
                Object other$description = other.getDescription();
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }

                Object this$createdAt = this.getCreatedAt();
                Object other$createdAt = other.getCreatedAt();
                if (this$createdAt == null) {
                    if (other$createdAt != null) {
                        return false;
                    }
                } else if (!this$createdAt.equals(other$createdAt)) {
                    return false;
                }

                label86: {
                    Object this$moderatorId = this.getModeratorId();
                    Object other$moderatorId = other.getModeratorId();
                    if (this$moderatorId == null) {
                        if (other$moderatorId == null) {
                            break label86;
                        }
                    } else if (this$moderatorId.equals(other$moderatorId)) {
                        break label86;
                    }

                    return false;
                }

                label79: {
                    Object this$moderatorName = this.getModeratorName();
                    Object other$moderatorName = other.getModeratorName();
                    if (this$moderatorName == null) {
                        if (other$moderatorName == null) {
                            break label79;
                        }
                    } else if (this$moderatorName.equals(other$moderatorName)) {
                        break label79;
                    }

                    return false;
                }

                Object this$acceptedAt = this.getAcceptedAt();
                Object other$acceptedAt = other.getAcceptedAt();
                if (this$acceptedAt == null) {
                    if (other$acceptedAt != null) {
                        return false;
                    }
                } else if (!this$acceptedAt.equals(other$acceptedAt)) {
                    return false;
                }

                Object this$status = this.getStatus();
                Object other$status = other.getStatus();
                if (this$status == null) {
                    if (other$status != null) {
                        return false;
                    }
                } else if (!this$status.equals(other$status)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Deal;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        long $price = Double.doubleToLongBits(this.getPrice());
        result = result * 59 + (int)($price >>> 32 ^ $price);
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $playerId = this.getPlayerId();
        result = result * 59 + ($playerId == null ? 43 : $playerId.hashCode());
        Object $playerName = this.getPlayerName();
        result = result * 59 + ($playerName == null ? 43 : $playerName.hashCode());
        Object $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        Object $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : $createdAt.hashCode());
        Object $moderatorId = this.getModeratorId();
        result = result * 59 + ($moderatorId == null ? 43 : $moderatorId.hashCode());
        Object $moderatorName = this.getModeratorName();
        result = result * 59 + ($moderatorName == null ? 43 : $moderatorName.hashCode());
        Object $acceptedAt = this.getAcceptedAt();
        result = result * 59 + ($acceptedAt == null ? 43 : $acceptedAt.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "Deal(id=" + this.getId() + ", playerId=" + this.getPlayerId() + ", playerName=" + this.getPlayerName() + ", description=" + this.getDescription() + ", createdAt=" + this.getCreatedAt() + ", price=" + this.getPrice() + ", moderatorId=" + this.getModeratorId() + ", moderatorName=" + this.getModeratorName() + ", acceptedAt=" + this.getAcceptedAt() + ", status=" + this.getStatus() + ")";
    }
}
