package org.rofu.eholygarant.storage.mysql;

import org.rofu.eholygarant.EHolyGarant;
import org.rofu.eholygarant.storage.sql.SQLStorage;

public class MySQLStorage extends SQLStorage {
    public MySQLStorage(EHolyGarant plugin, String host, int port, String database,
                        String username, String password, boolean useSSL) {
        super(plugin,
                "jdbc:mysql://" + host + ":" + port + "/" + database +
                        "?useSSL=" + useSSL + "&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8",
                username,
                password);
    }

    @Override
    protected void loadDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }
}