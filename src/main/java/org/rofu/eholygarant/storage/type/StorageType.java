package org.rofu.eholygarant.storage.type;

import org.bukkit.configuration.ConfigurationSection;
import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.storage.Storage;
import org.rofu.eholygarant.storage.mysql.MySQLStorage;
import org.rofu.eholygarant.storage.sqlite.SQLiteStorage;
import org.rofu.eholygarant.storage.yaml.YamlStorage;

public enum StorageType {
    YAML {
        @Override
        public Storage createStorage(EHolyGarant plugin, ConfigurationSection config) {
            return new YamlStorage(plugin);
        }
    },
    MYSQL {
        @Override
        public Storage createStorage(EHolyGarant plugin, ConfigurationSection config) {
            ConfigurationSection mysql = config.getConfigurationSection("mysql");
            if (mysql == null) {
                plugin.getLogger().warning("MySQL section missing, falling back to YAML");
                return YAML.createStorage(plugin, config);
            }
            return new MySQLStorage(plugin,
                    mysql.getString("host", "localhost"),
                    mysql.getInt("port", 3306),
                    mysql.getString("database", "garant"),
                    mysql.getString("username", "root"),
                    mysql.getString("password", ""),
                    mysql.getBoolean("useSSL", false));
        }
    },
    SQLITE {
        @Override
        public Storage createStorage(EHolyGarant plugin, ConfigurationSection config) {
            return new SQLiteStorage(plugin);
        }
    };

    public abstract Storage createStorage(EHolyGarant plugin, ConfigurationSection config);

    public static StorageType fromString(String type) {
        try {
            return valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return YAML;
        }
    }
}