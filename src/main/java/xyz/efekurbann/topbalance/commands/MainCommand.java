package xyz.efekurbann.topbalance.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.efekurbann.topbalance.TopBalancePlugin;
import xyz.efekurbann.topbalance.menus.TopMenu;
import xyz.efekurbann.topbalance.objects.TopPlayer;
import xyz.efekurbann.topbalance.utils.ConfigManager;
import xyz.efekurbann.topbalance.utils.Tools;

public class MainCommand implements CommandExecutor {

    private final TopBalancePlugin plugin;
    private final FileConfiguration config = ConfigManager.get("config.yml");

    public MainCommand(TopBalancePlugin plugin){
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You can not execute this command from console. Available args: reload");
                return false;
            }
            Player player = (Player) sender;

            if (config.getBoolean("Gui.enabled")) {
                TopMenu menu = new TopMenu(
                        plugin.getInventoryAPI(),
                        "topmenu",
                        Tools.colored(config.getString("Gui.title")),
                        config.getInt("Gui.size"));

                menu.open(player);

                return true;
            }

            player.sendMessage(Tools.colored(config.getString("Messages.chat-baltop-header")));
            for (String key : config.getConfigurationSection("Tops").getKeys(false)) {
                int number = config.getInt("Tops." + key + ".order");
                TopPlayer topPlayer = plugin.getPlayersMap().get(number - 1);
                if (topPlayer != null) {
                    player.sendMessage(Tools.colored(config.getString("Messages.chat-baltop-format")
                            .replace("{name}", topPlayer.getName())
                            .replace("{balance}", Tools.formatMoney(topPlayer.getBalance()))
                            .replace("{balance_raw}", String.valueOf(topPlayer.getBalance()))
                            .replace("{order}", String.valueOf(number))));
                }
            }
            player.sendMessage(Tools.colored(config.getString("Messages.chat-baltop-footer")));

            return true;
        } else if (args.length == 1 && args[0].equals("reload")){
            if (sender.hasPermission("topbalance.admin")){
                Tools.reload();
                sender.sendMessage(Tools.colored(config.getString("Messages.reloaded")));
                return true;
            }
            sender.sendMessage(Tools.colored(config.getString("Messages.no-perm")));
        } else {
            sender.sendMessage(ChatColor.RED + "Wrong usage!");
        }
        return false;
    }
}
