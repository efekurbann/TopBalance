package xyz.efekurbann.topbalance.tasks;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.efekurbann.topbalance.TopBalancePlugin;
import xyz.efekurbann.topbalance.objects.TopPlayer;
import xyz.efekurbann.topbalance.utils.ConfigManager;
import xyz.efekurbann.topbalance.utils.VaultManager;

import java.util.*;
import java.util.stream.Collectors;

public class UpdateTop extends BukkitRunnable {

    private final TopBalancePlugin plugin;

    public UpdateTop(TopBalancePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getLogger().info("Task started, updating..");
        long start = System.currentTimeMillis();
        Map<OfflinePlayer, Double> balances = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) balances.put(player, VaultManager.getEcon().getBalance((OfflinePlayer) player));
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
            if (VaultManager.getEcon().hasAccount(player)) balances.put(player, VaultManager.getEcon().getBalance(player));
        }

        Map<OfflinePlayer, Double> sortedMap = balances.entrySet().stream().sorted((o1, o2) -> (o2.getValue()).compareTo(o1.getValue())).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        int i = 0;
        int max = ConfigManager.get("config.yml").getConfigurationSection("Tops").getKeys(false).size();
        Iterator<Map.Entry<OfflinePlayer, Double>> iterator = sortedMap.entrySet().iterator();
        plugin.getPlayersMap().clear();
        while (iterator.hasNext()){
            if (!(i < max)) break;
            Map.Entry<OfflinePlayer, Double> entry = iterator.next();
            TopPlayer player = new TopPlayer(entry.getKey().getName(), entry.getKey().getUniqueId(), entry.getValue());
            plugin.getPlayersMap().put(i, player);
            i++;
        }

        plugin.getLogger().info("Task is over, successfully updated top players. Took " + (System.currentTimeMillis() - start) + "ms [ "+ sortedMap.size() +" player(s) ]");

    }
}
