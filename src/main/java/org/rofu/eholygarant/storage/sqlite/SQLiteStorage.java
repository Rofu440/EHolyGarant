package org.rofu.eholygarant.storage.sqlite;

import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.storage.sql.SQLStorage;

import java.io.File;
import java.io.IOException;

public class SQLiteStorage extends SQLStorage {
    public SQLiteStorage(EHolyGarant plugin) {
        super(plugin, "jdbc:sqlite:" + new File(plugin.getDataFolder(), "database.db").getAbsolutePath(), null, null);
    }

    @Override
    protected void loadDriver() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @Override
    public void init() {
        plugin.getDataFolder().mkdirs();
        File dbFile = new File(plugin.getDataFolder(), "database.db");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create database file");
            }
        }
        super.init();
    }
}