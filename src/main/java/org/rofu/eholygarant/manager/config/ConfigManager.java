package org.rofu.eholygarant.manager.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.rofu.eholygarant.EHolyGarant;

import java.util.List;
import java.util.Objects;

public class ConfigManager {
    private final EHolyGarant plugin;
    private String menuName;
    private int menuSize;
    private Material sendMaterial;
    private String sendName;
    private int sendPos;
    private double price;
    private Material rulesMaterial;
    private String rulesName;
    private List<String> rulesLore;
    private int rulesPos;
    private String listTitle;
    private boolean textWrappingEnabled;
    private int maxLineLength;
    private String firstLineFormatActive;
    private String continuationLineFormatActive;
    private String firstLineFormatDeal;
    private String continuationLineFormatDeal;
    private int searchTime;
    private int visitTime;
    private Messages messages;

    public ConfigManager(EHolyGarant plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection menuSection = config.getConfigurationSection("menu");
        if (menuSection != null) {
            menuName = menuSection.getString("name", "Найти Гаранта сделки");
            menuSize = menuSection.getInt("size", 27);
            sendMaterial = Material.valueOf(menuSection.getString("material-send", "LIME_WOOL"));
            sendName = menuSection.getString("name-send", "&#05FB00▶ Создать заявку");
            sendPos = menuSection.getInt("pos-send", 13);
            price = menuSection.getDouble("price", 125000.0);
            rulesMaterial = Material.valueOf(menuSection.getString("material-rules", "PAPER"));
            rulesName = menuSection.getString("name-rules", "&0 ");
            rulesLore = menuSection.getStringList("lore-rules");
            rulesPos = menuSection.getInt("pos-rule", 8);
        }

        ConfigurationSection listSection = config.getConfigurationSection("menu_list");
        if (listSection != null) {
            listTitle = listSection.getString("title", "Список заявок на сделку");
            ConfigurationSection wrapping = listSection.getConfigurationSection("text_wrapping");
            if (wrapping != null) {
                textWrappingEnabled = wrapping.getBoolean("enabled", true);
                maxLineLength = wrapping.getInt("max_line_length", 50);
                firstLineFormatActive = wrapping.getString("first_line_format_active");
                continuationLineFormatActive = wrapping.getString("continuation_line_format_active");
                firstLineFormatDeal = wrapping.getString("first_line_format_deal");
                continuationLineFormatDeal = wrapping.getString("continuation_line_format_deal");
            }
        }

        ConfigurationSection timeSection = config.getConfigurationSection("time");
        if (timeSection != null) {
            searchTime = timeSection.getInt("search-time", 600);
            visitTime = timeSection.getInt("visit-time", 600);
        }

        messages = new Messages(Objects.requireNonNullElse(config.getConfigurationSection("messages"), config.createSection("messages")));
    }

    public String getDecorationMaterial() {
        return plugin.getConfig().getString("menu_list.items.decoration.material", "BLUE_STAINED_GLASS_PANE");
    }

    public List<Integer> getDecorationSlots() {
        return plugin.getConfig().getIntegerList("menu_list.items.decoration.slots");
    }

    public ConfigurationSection getItemSection(String path) {
        return plugin.getConfig().getConfigurationSection("menu_list.items." + path);
    }

    public String getMenuName() { return menuName; }
    public int getMenuSize() { return menuSize; }
    public Material getSendMaterial() { return sendMaterial; }
    public String getSendName() { return sendName; }
    public int getSendPos() { return sendPos; }
    public double getPrice() { return price; }
    public Material getRulesMaterial() { return rulesMaterial; }
    public String getRulesName() { return rulesName; }
    public List<String> getRulesLore() { return rulesLore; }
    public int getRulesPos() { return rulesPos; }
    public String getListTitle() { return listTitle; }
    public boolean isTextWrappingEnabled() { return textWrappingEnabled; }
    public int getMaxLineLength() { return maxLineLength; }
    public String getFirstLineFormatActive() { return firstLineFormatActive; }
    public String getContinuationLineFormatActive() { return continuationLineFormatActive; }
    public String getFirstLineFormatDeal() { return firstLineFormatDeal; }
    public String getContinuationLineFormatDeal() { return continuationLineFormatDeal; }
    public int getSearchTime() { return searchTime; }
    public int getVisitTime() { return visitTime; }
    public Messages getMessages() { return messages; }
}