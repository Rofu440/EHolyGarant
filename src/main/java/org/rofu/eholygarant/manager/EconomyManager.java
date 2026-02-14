package org.rofu.eholygarant.manager;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.rofu.eholygarant.EHolyGarant;

public class EconomyManager {
    private final EHolyGarant plugin;
    private Economy economy;

    public EconomyManager(EHolyGarant plugin) {
        this.plugin = plugin;
        this.setupEconomy();
    }

    private void setupEconomy() {
        if (this.plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                this.plugin.getLogger().severe("vault либо essentials не был найден на сервере");
            } else {
                this.economy = (Economy)rsp.getProvider();
            }
        }
    }

    public boolean hasEnough(Player player, double amount) {
        return this.economy == null ? false : this.economy.has(player, amount);
    }

    public boolean withdraw(Player player, double amount) {
        if (this.economy == null) {
            return false;
        } else {
            EconomyResponse response = this.economy.withdrawPlayer(player, amount);
            return response.transactionSuccess();
        }
    }

    public boolean deposit(Player player, double amount) {
        if (this.economy == null) {
            return false;
        } else {
            EconomyResponse response = this.economy.depositPlayer(player, amount);
            return response.transactionSuccess();
        }
    }

    public double getBalance(Player player) {
        return this.economy == null ? 0.0 : this.economy.getBalance(player);
    }

    public String format(double amount) {
        return this.economy == null ? String.valueOf(amount) : this.economy.format(amount);
    }

    public boolean isEnabled() {
        return this.economy != null;
    }
}

