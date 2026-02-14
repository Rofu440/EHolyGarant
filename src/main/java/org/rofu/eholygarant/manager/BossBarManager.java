package org.rofu.eholygarant.manager;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.core.util.ColorUtil;


public class BossBarManager {
    private final EHolyGarant plugin;
    private final Map<UUID, BossBar> playerBossBars;

    public BossBarManager(EHolyGarant plugin) {
        this.plugin = plugin;
        this.playerBossBars = new ConcurrentHashMap();
    }

    public void showSearchBar(Player player, int remainingSeconds) {
        String text = this.plugin.getConfigManager().getMessages().getBossBarText("search").replace("%time_search%", this.formatTime(remainingSeconds));
        String color = this.plugin.getConfigManager().getMessages().getBossBarColor("search");
        String style = this.plugin.getConfigManager().getMessages().getBossBarStyle("search");
        this.createOrUpdateBar(player, text, color, style, (double)remainingSeconds / (double)this.plugin.getConfigManager().getSearchTime());
    }

    public void showDealBar(Player player) {
        String text = this.plugin.getConfigManager().getMessages().getBossBarText("deal");
        String color = this.plugin.getConfigManager().getMessages().getBossBarColor("deal");
        String style = this.plugin.getConfigManager().getMessages().getBossBarStyle("deal");
        this.createOrUpdateBar(player, text, color, style, 1.0);
    }

    public void showModeratorBar(Player player) {
        String text = this.plugin.getConfigManager().getMessages().getBossBarText("moderator");
        String color = this.plugin.getConfigManager().getMessages().getBossBarColor("moderator");
        String style = this.plugin.getConfigManager().getMessages().getBossBarStyle("moderator");
        this.createOrUpdateBar(player, text, color, style, 1.0);
    }

    private void createOrUpdateBar(Player player, String text, String color, String style, double progress) {
        BossBar bar = (BossBar)this.playerBossBars.get(player.getUniqueId());
        if (bar == null) {
            bar = Bukkit.createBossBar(ColorUtil.colorize(text), BarColor.valueOf(color), BarStyle.valueOf(style), new BarFlag[0]);
            bar.addPlayer(player);
            this.playerBossBars.put(player.getUniqueId(), bar);
        } else {
            bar.setTitle(ColorUtil.colorize(text));
            bar.setColor(BarColor.valueOf(color));
            bar.setStyle(BarStyle.valueOf(style));
        }

        bar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
        bar.setVisible(true);
    }

    public void updateProgress(Player player, double progress) {
        BossBar bar = (BossBar)this.playerBossBars.get(player.getUniqueId());
        if (bar != null) {
            bar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
        }

    }

    public void remove(Player player) {
        BossBar bar = (BossBar)this.playerBossBars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removePlayer(player);
            bar.setVisible(false);
        }

    }

    public void removeAll() {
        Iterator var1 = this.playerBossBars.entrySet().iterator();

        while(var1.hasNext()) {
            Map.Entry<UUID, BossBar> entry = (Map.Entry)var1.next();
            BossBar bar = (BossBar)entry.getValue();
            bar.removeAll();
            bar.setVisible(false);
        }

        this.playerBossBars.clear();
    }

    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", mins, secs);
    }

    public boolean hasBar(UUID playerId) {
        return this.playerBossBars.containsKey(playerId);
    }
}

