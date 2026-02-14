package org.rofu.eholygarant.core.util;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.rofu.eholygarant.EHolyGarant;

public final class SoundUtil {
    private SoundUtil() {
    }

    public static void playSound(Player player, String soundKey) {
        if (player != null && player.isOnline()) {
            EHolyGarant plugin = EHolyGarant.getInstance();
            ConfigurationSection soundsSection = plugin.getConfig().getConfigurationSection("sounds");
            if (soundsSection != null && soundsSection.getBoolean("enabled", true)) {
                ConfigurationSection soundConfig = soundsSection.getConfigurationSection(soundKey);
                if (soundConfig != null) {
                    String soundName = soundConfig.getString("sound", "BLOCK_NOTE_BLOCK_PLING");
                    float volume = (float)soundConfig.getDouble("volume", 1.0);
                    float pitch = (float)soundConfig.getDouble("pitch", 1.0);

                    try {
                        Sound sound = Sound.valueOf(soundName.toUpperCase());
                        player.playSound(player.getLocation(), sound, volume, pitch);
                    } catch (IllegalArgumentException var9) {
                        plugin.getLogger().warning("Invalid sound: " + soundName + " for key: " + soundKey);
                    }

                }
            }
        }
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        if (player != null && player.isOnline()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}

