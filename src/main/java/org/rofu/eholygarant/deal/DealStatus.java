package org.rofu.eholygarant.deal;

public enum DealStatus {
    WAITING("Ожидает гаранта"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершена"),
    CANCELLED("Отменена"),
    TIMEOUT("Время истекло");

    private final String displayName;

    public boolean isActive() {
        return this == WAITING || this == IN_PROGRESS;
    }

    public boolean isFinished() {
        return this == COMPLETED || this == CANCELLED || this == TIMEOUT;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    private DealStatus(String displayName) {
        this.displayName = displayName;
    }
}
