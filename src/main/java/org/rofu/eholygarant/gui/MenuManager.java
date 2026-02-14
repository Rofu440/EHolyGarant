package org.rofu.eholygarant.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.rofu.eholygarant.EHolyGarant;

public class MenuManager {
    private final EHolyGarant plugin;
    private final Map<UUID, AbstractMenu> openMenus;

    public MenuManager(EHolyGarant plugin) {
        this.plugin = plugin;
        this.openMenus = new HashMap();
    }

    public void openCreateDealMenu(Player player) {
        CreateDealMenu menu = new CreateDealMenu(this.plugin, player);
        menu.open();
        this.openMenus.put(player.getUniqueId(), menu);
    }

    public void openDealListMenu(Player player) {
        DealListMenu menu = new DealListMenu(this.plugin, player);
        menu.open();
        this.openMenus.put(player.getUniqueId(), menu);
    }

    public void openDealListMenu(Player player, int page) {
        DealListMenu menu = new DealListMenu(this.plugin, player, page);
        menu.open();
        this.openMenus.put(player.getUniqueId(), menu);
    }

    public AbstractMenu getOpenMenu(UUID playerId) {
        return (AbstractMenu)this.openMenus.get(playerId);
    }

    public void removeMenu(UUID playerId) {
        this.openMenus.remove(playerId);
    }

    public boolean hasOpenMenu(UUID playerId) {
        return this.openMenus.containsKey(playerId);
    }
}

