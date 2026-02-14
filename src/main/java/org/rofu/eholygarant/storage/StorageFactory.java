package org.rofu.eholygarant.storage;

import org.bukkit.configuration.ConfigurationSection;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.storage.type.StorageType;
import org.rofu.eholygarant.storage.yaml.YamlStorage;

public final class StorageFactory {
    private StorageFactory() {}

    public static Storage createStorage(EHolyGarant plugin) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("storage");
        if (config == null) {
            plugin.getLogger().warning("Storage section not found, using YAML");
            return new YamlStorage(plugin);
        }
        String typeStr = config.getString("type", "YAML");
        StorageType type = StorageType.fromString(typeStr);
        return type.createStorage(plugin, config);
    }
}