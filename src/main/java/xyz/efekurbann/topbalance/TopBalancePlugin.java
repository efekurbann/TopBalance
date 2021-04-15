package xyz.efekurbann.topbalance;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.efekurbann.inventory.InventoryAPI;
import xyz.efekurbann.topbalance.commands.MainCommand;
import xyz.efekurbann.topbalance.objects.TopPlayer;
import xyz.efekurbann.topbalance.tasks.UpdateTop;
import xyz.efekurbann.topbalance.utils.ConfigManager;
import xyz.efekurbann.topbalance.utils.VaultManager;

import java.util.HashMap;

public final class TopBalancePlugin extends JavaPlugin {

    private static TopBalancePlugin instance;
    private final HashMap<Integer, TopPlayer> players = new HashMap<>();
    private InventoryAPI inventoryAPI;
    private UpdateTop updateTopTask;

    @Override
    public void onEnable() {
        instance = this;
        if (!VaultManager.setupEconomy()){
            getLogger().severe("Vault-Eco not found! Plugin is disabled..");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        inventoryAPI = new InventoryAPI(this);

        ConfigManager.load("config.yml");

        updateTopTask = new UpdateTop(this);
        updateTopTask.runTaskTimerAsynchronously(this, 10L, 12000L);

        getCommand("baltop").setExecutor(new MainCommand(this));
        getLogger().info("Plugin enabled. Thank you for using.");
        new Metrics(this, 11047);

    }

    public InventoryAPI getInventoryAPI() {
        return inventoryAPI;
    }

    @Override
    public void onDisable() {

    }

    public HashMap<Integer, TopPlayer> getPlayersMap() {
        return players;
    }

    public UpdateTop getUpdateTopTask() {
        return updateTopTask;
    }

    public static TopBalancePlugin getInstance() {
        return instance;
    }

}
