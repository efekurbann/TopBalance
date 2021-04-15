package xyz.efekurbann.topbalance.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import xyz.efekurbann.topbalance.TopBalancePlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final Plugin plugin = TopBalancePlugin.getInstance();
    private static final Map<String, FileConfiguration> configs = new HashMap<>();

    public static boolean isFileLoaded(String fileName) {
        return configs.containsKey(fileName);
    }

    public static void load(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                plugin.saveResource(name, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!isFileLoaded(name)) configs.put(name, YamlConfiguration.loadConfiguration(file));
    }

    public static FileConfiguration get(String name) {
        if (isFileLoaded(name)) {
            return configs.get(name);
        }
        return null;
    }

    public static void set(String fileName, String path, Object value) {
        if (isFileLoaded(fileName)) {
            configs.get(fileName).set(path, value);
        }
    }

    public static void reload(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        try {
            configs.get(fileName).load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        try {
            configs.get(fileName).save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
