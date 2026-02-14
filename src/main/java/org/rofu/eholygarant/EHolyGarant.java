package org.rofu.eholygarant;

import org.bukkit.plugin.java.JavaPlugin;
import org.rofu.eholygarant.core.command.GarantCommand;
import org.rofu.eholygarant.manager.config.ConfigManager;
import org.rofu.eholygarant.deal.DealManager;
import org.rofu.eholygarant.gui.MenuManager;
import org.rofu.eholygarant.core.listener.ChatListener;
import org.rofu.eholygarant.gui.listener.MenuListener;
import org.rofu.eholygarant.core.listener.PlayerListener;
import org.rofu.eholygarant.manager.BossBarManager;
import org.rofu.eholygarant.manager.EconomyManager;
import org.rofu.eholygarant.manager.StatsManager;
import org.rofu.eholygarant.storage.Storage;
import org.rofu.eholygarant.storage.StorageFactory;

public final class EHolyGarant extends JavaPlugin {
    private static EHolyGarant instance;
    private ConfigManager configManager;
    private EconomyManager economyManager;
    private DealManager dealManager;
    private MenuManager menuManager;
    private BossBarManager bossBarManager;
    private StatsManager statsManager;
    private Storage storage;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        storage = StorageFactory.createStorage(this);
        storage.init();
        economyManager = new EconomyManager(this);
        bossBarManager = new BossBarManager(this);
        dealManager = new DealManager(this);
        menuManager = new MenuManager(this);
        statsManager = new StatsManager(this);
        getCommand("garant").setExecutor(new GarantCommand(this));
        getCommand("garant").setTabCompleter(new GarantCommand(this));
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        if (dealManager != null) dealManager.shutdown();
        if (bossBarManager != null) bossBarManager.removeAll();
        if (storage != null) storage.shutdown();
    }

    public void reload() {
        reloadConfig();
        configManager.reload();
    }

    public static EHolyGarant getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public EconomyManager getEconomyManager() { return economyManager; }
    public DealManager getDealManager() { return dealManager; }
    public MenuManager getMenuManager() { return menuManager; }
    public BossBarManager getBossBarManager() { return bossBarManager; }
    public StatsManager getStatsManager() { return statsManager; }
    public Storage getStorage() { return storage; }
}