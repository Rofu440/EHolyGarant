package org.rofu.eholygarant.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.core.util.*;

public abstract class AbstractMenu implements InventoryHolder {
    protected final EHolyGarant plugin;
    protected final Player player;
    protected Inventory inventory;
    protected final Map<Integer, Consumer<InventoryClickEvent>> clickActions;

    public AbstractMenu(EHolyGarant plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.clickActions = new HashMap();
    }

    protected abstract String getTitle();

    protected abstract int getSize();

    protected abstract void setupItems();

    public void open() {
        this.inventory = Bukkit.createInventory(this, this.getSize(), ColorUtil.colorize(this.getTitle()));
        this.setupItems();
        this.player.openInventory(this.inventory);
    }

    public void close() {
        this.player.closeInventory();
    }

    public void refresh() {
        this.setupItems();
    }

    protected void setItem(int slot, ItemStack item) {
        this.inventory.setItem(slot, item);
    }

    protected void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        this.inventory.setItem(slot, item);
        this.clickActions.put(slot, action);
    }

    protected void fillBorder(ItemStack item) {
        int size = this.getSize();

        int i;
        for(i = 0; i < 9; ++i) {
            this.setItem(i, item);
        }

        for(i = size - 9; i < size; ++i) {
            this.setItem(i, item);
        }

        for(i = 9; i < size - 9; i += 9) {
            this.setItem(i, item);
            this.setItem(i + 8, item);
        }

    }

    protected void fill(ItemStack item) {
        for(int i = 0; i < this.getSize(); ++i) {
            if (this.inventory.getItem(i) == null) {
                this.setItem(i, item);
            }
        }

    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Consumer<InventoryClickEvent> action = (Consumer)this.clickActions.get(slot);
        if (action != null) {
            action.accept(event);
        }

    }

    protected ItemBuilder itemBuilder() {
        return new ItemBuilder();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public EHolyGarant getPlugin() {
        return this.plugin;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Map<Integer, Consumer<InventoryClickEvent>> getClickActions() {
        return this.clickActions;
    }
}

