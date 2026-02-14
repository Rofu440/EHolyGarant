package org.rofu.eholygarant.core.model;

import org.bukkit.entity.Player;
import org.rofu.eholygarant.EHolyGarant;

public enum PlayerState {
    NONE("Нет") {
        @Override
        public void handle(Player player, String input, EHolyGarant plugin) {
        }
    },
    ENTERING_DESCRIPTION("Ввод описания") {
        @Override
        public void handle(Player player, String input, EHolyGarant plugin) {
            plugin.getDealManager().setPlayerState(player.getUniqueId(), NONE);
            if (plugin.getDealManager().hasActiveDeal(player.getUniqueId())) {
                player.sendMessage(plugin.getConfigManager().getMessages().get("already-created"));
                return;
            }
            double price = plugin.getConfigManager().getPrice();
            if (!plugin.getEconomyManager().hasEnough(player, price)) {
                player.sendMessage(plugin.getConfigManager().getMessages().get("no-money",
                        "%amount%", plugin.getEconomyManager().format(price)));
                return;
            }
            plugin.getDealManager().createDeal(player.getUniqueId(), input)
                    .ifPresent(deal -> player.sendMessage(plugin.getConfigManager().getMessages().get("wait")));
        }
    },
    ENTERING_CANCEL_REASON("Ввод причины отмены") {
        @Override
        public void handle(Player player, String input, EHolyGarant plugin) {
            plugin.getDealManager().setCancelReason(player.getUniqueId(), input);
        }
    };

    private final String displayName;

    PlayerState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract void handle(Player player, String input, EHolyGarant plugin);
}