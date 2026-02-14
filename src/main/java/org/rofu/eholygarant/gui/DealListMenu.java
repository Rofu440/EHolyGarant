package org.rofu.eholygarant.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.manager.config.*;
import org.rofu.eholygarant.deal.*;
import org.rofu.eholygarant.core.model.*;
import org.rofu.eholygarant.core.util.*;


public class DealListMenu extends AbstractMenu {
    private static final int ITEMS_PER_PAGE = 45;
    private int currentPage;

    public DealListMenu(EHolyGarant plugin, Player player) {
        this(plugin, player, 0);
    }

    public DealListMenu(EHolyGarant plugin, Player player, int page) {
        super(plugin, player);
        this.currentPage = page;
    }

    protected String getTitle() {
        return this.plugin.getConfigManager().getListTitle();
    }

    protected int getSize() {
        return 54;
    }

    protected void setupItems() {
        this.inventory.clear();
        this.clickActions.clear();
        this.setupDecoration();
        this.setupNavigationItems();
        this.setupStatsItem();
        this.setupActiveDealItem();
        this.setupDealItems();
    }

    private void setupDecoration() {
        ConfigManager config = this.plugin.getConfigManager();
        String material = config.getDecorationMaterial();
        List<Integer> slots = config.getDecorationSlots();
        ItemStack decorItem = this.itemBuilder().material(material).name("&r").build();
        Iterator var5 = slots.iterator();

        while(var5.hasNext()) {
            int slot = (Integer)var5.next();
            this.setItem(slot, decorItem);
        }

    }

    private void setupNavigationItems() {
        ConfigurationSection nextSection = this.plugin.getConfigManager().getItemSection("next_page");
        ConfigurationSection prevSection = this.plugin.getConfigManager().getItemSection("prev_page");
        ItemStack prevItem;
        if (nextSection != null) {
            prevItem = this.itemBuilder().material(nextSection.getString("material", "NETHER_STAR")).name(nextSection.getString("display_name", "&bслед страница")).build();
            this.setItem(nextSection.getInt("slot", 53), prevItem, (event) -> {
                SoundUtil.playSound(this.player, "menu-click");
                List<Deal> deals = this.getWaitingDeals();
                int maxPage = (deals.size() - 1) / 45;
                if (this.currentPage < maxPage) {
                    this.plugin.getMenuManager().openDealListMenu(this.player, this.currentPage + 1);
                }

            });
        }

        if (prevSection != null) {
            prevItem = this.itemBuilder().material(prevSection.getString("material", "NETHER_STAR")).name(prevSection.getString("display_name", "&bпрошлая страцина")).build();
            this.setItem(prevSection.getInt("slot", 45), prevItem, (event) -> {
                SoundUtil.playSound(this.player, "menu-click");
                if (this.currentPage > 0) {
                    this.plugin.getMenuManager().openDealListMenu(this.player, this.currentPage - 1);
                }

            });
        }

    }

    private void setupStatsItem() {
        ConfigurationSection section = this.plugin.getConfigManager().getItemSection("stats_item");
        if (section != null) {
            DealStats stats = this.plugin.getStatsManager().getStats(this.player.getUniqueId());
            List<String> lore = new ArrayList();
            Iterator var4 = section.getStringList("lore").iterator();

            while(var4.hasNext()) {
                String line = (String)var4.next();
                lore.add(line.replace("%success%", String.valueOf(stats.getSuccessCount())).replace("%canceled%", String.valueOf(stats.getCancelledCount())).replace("%earned%", String.valueOf((int)stats.getTotalEarned())));
            }

            ItemStack item = this.itemBuilder().material(section.getString("material", "PLAYER_HEAD")).name(section.getString("display_name", "&r")).lore(lore).skullOwner(this.player.getUniqueId()).build();
            this.setItem(section.getInt("slot", 49), item);
        }
    }

    private void setupActiveDealItem() {
        Optional<Deal> activeDeal = this.plugin.getDealManager().getActiveDealByModerator(this.player.getUniqueId());
        if (activeDeal.isPresent()) {
            this.setupActiveActiveDealItem((Deal)activeDeal.get());
        } else {
            this.setupNoActiveDealItem();
        }

    }

    private void setupActiveActiveDealItem(Deal deal) {
        ConfigurationSection section = this.plugin.getConfigManager().getItemSection("active_deal");
        if (section != null) {
            ConfigManager config = this.plugin.getConfigManager();
            TextWrapper wrapper = TextWrapper.fromConfig(config.getMaxLineLength(), config.getFirstLineFormatActive(), config.getContinuationLineFormatActive());
            List<String> wrappedDesc = wrapper.wrap(deal.getDescription());
            List<String> lore = new ArrayList();
            Iterator var7 = section.getStringList("lore").iterator();

            while(var7.hasNext()) {
                String line = (String)var7.next();
                if (line.contains("%wrapped_description%")) {
                    lore.addAll(wrappedDesc);
                } else {
                    lore.add(line.replace("%player_name%", deal.getPlayerName()).replace("%time_data%", deal.getFormattedCreatedAt()).replace("%player_name_moder%", this.player.getName()));
                }
            }

            ItemStack item = this.itemBuilder().material(section.getString("material", "ZOMBIE_HEAD")).name(section.getString("display_name", "&fактивная сделка").replace("%player_name%", deal.getPlayerName())).lore(lore).skullOwner(deal.getPlayerId()).build();
            this.setItem(section.getInt("slot", 46), item, (event) -> {
                SoundUtil.playSound(this.player, "menu-click");
                if (event.getClick() == ClickType.RIGHT) {
                    this.plugin.getDealManager().setPlayerState(this.player.getUniqueId(), PlayerState.ENTERING_CANCEL_REASON);
                    this.player.closeInventory();
                    this.player.sendMessage(this.plugin.getConfigManager().getMessages().get("cancel-prompt"));
                } else if (event.getClick() == ClickType.LEFT) {
                    this.plugin.getDealManager().completeDeal(deal.getId());
                    this.player.closeInventory();
                }

            });
        }
    }

    private void setupNoActiveDealItem() {
        ConfigurationSection section = this.plugin.getConfigManager().getItemSection("no_active_deal");
        if (section != null) {
            ItemStack item = this.itemBuilder().material(section.getString("material", "BARRIER")).name(section.getString("display_name", "&cне активная сделка")).lore(section.getStringList("lore")).build();
            this.setItem(section.getInt("slot", 46), item);
        }
    }

    private void setupDealItems() {
        List<Deal> deals = this.getWaitingDeals();
        int startIndex = this.currentPage * 45;
        int endIndex = Math.min(startIndex + 45, deals.size());
        ConfigurationSection section = this.plugin.getConfigManager().getItemSection("deal_item");
        if (section != null) {
            ConfigManager config = this.plugin.getConfigManager();
            TextWrapper wrapper = TextWrapper.fromConfig(config.getMaxLineLength(), config.getFirstLineFormatDeal(), config.getContinuationLineFormatDeal());
            int slot = 0;

            for(int i = startIndex; i < endIndex && slot < 45; ++i) {
                Deal deal;
                for(deal = (Deal)deals.get(i); this.isReservedSlot(slot) && slot < 45; ++slot) {
                }

                if (slot >= 45) {
                    break;
                }

                List<String> wrappedDesc = wrapper.wrap(deal.getDescription());
                List<String> lore = new ArrayList();
                Iterator var12 = section.getStringList("lore").iterator();

                while(var12.hasNext()) {
                    String line = (String)var12.next();
                    if (line.contains("%wrapped_description%")) {
                        lore.addAll(wrappedDesc);
                    } else {
                        lore.add(line.replace("%player_name%", deal.getPlayerName()).replace("%time_data%", deal.getFormattedCreatedAt()));
                    }
                }

                ItemStack item = this.itemBuilder().material(section.getString("material", "ZOMBIE_HEAD")).name(section.getString("display_name", "&fтипо сделка").replace("%player_name%", deal.getPlayerName())).lore(lore).skullOwner(deal.getPlayerId()).build();
                this.setItem(slot, item, (event) -> {
                    SoundUtil.playSound(this.player, "menu-click");
                    if (event.getClick() == ClickType.LEFT) {
                        if (deal.isOwnedBy(this.player.getUniqueId())) {
                            this.player.sendMessage(this.plugin.getConfigManager().getMessages().get("cannot-accept-own"));
                            SoundUtil.playSound(this.player, "error");
                            return;
                        }

                        if (this.plugin.getDealManager().isModerating(this.player.getUniqueId())) {
                            SoundUtil.playSound(this.player, "error");
                            return;
                        }

                        boolean accepted = this.plugin.getDealManager().acceptDeal(this.player.getUniqueId(), deal.getId());
                        if (accepted) {
                            this.player.sendMessage(this.plugin.getConfigManager().getMessages().get("deal-accepted", new Object[]{"%player%", deal.getPlayerName()}));
                            this.refresh();
                        }
                    }

                });
                ++slot;
            }

        }
    }

    private boolean isReservedSlot(int slot) {
        List<Integer> decorSlots = this.plugin.getConfigManager().getDecorationSlots();
        if (decorSlots.contains(slot)) {
            return true;
        } else if (slot != 45 && slot != 46 && slot != 49 && slot != 53) {
            return slot >= 45;
        } else {
            return true;
        }
    }

    private List<Deal> getWaitingDeals() {
        return this.plugin.getDealManager().getDealsByStatus(DealStatus.WAITING);
    }
}

