package xyz.efekurbann.topbalance.menus;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.efekurbann.inventory.GUI;
import xyz.efekurbann.inventory.Hytem;
import xyz.efekurbann.inventory.InventoryAPI;
import xyz.efekurbann.topbalance.TopBalancePlugin;
import xyz.efekurbann.topbalance.objects.TopPlayer;
import xyz.efekurbann.topbalance.utils.ConfigManager;
import xyz.efekurbann.topbalance.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class TopMenu extends GUI {

    private final TopBalancePlugin plugin;
    private final FileConfiguration config = ConfigManager.get("config.yml");

    public TopMenu(InventoryAPI api, String id, String title, int size) {
        super(api, id, title, size);
        this.plugin = TopBalancePlugin.getInstance();

        create();
    }

    public void create() {

        if (config.getBoolean("Gui.items.fill.enabled")){
            for (int i = 0; i < getSize(); i++){
                addItem(i, new ItemBuilder(
                        XMaterial.matchXMaterial(config.getString("Gui.items.fill.material")).get().parseItem()).withName(" ").build());
            }
        }

        for (String key : config.getConfigurationSection("Tops").getKeys(false)) {
            int number = config.getInt("Tops." + key + ".order");
            TopPlayer player = plugin.getPlayersMap().get(number-1);
            int slot = config.getInt("Tops." + key + ".slot");
            ItemStack item;
            if (player != null) {
                if (config.getBoolean("Gui.custom-item")) {
                    List<String> lore = new ArrayList<>();

                    for (String str : config.getStringList("Gui.items.player-item.lore")) {
                        lore.add(str.replace("{order}", String.valueOf(number))
                                .replace("{name}", player.getName())
                                .replace("{balance_raw}", String.valueOf(player.getBalance()))
                                .replace("{balance}", Tools.formatMoney(player.getBalance())));
                    }
                    item = new ItemBuilder(XMaterial.valueOf(config.getString("Tops." + key + ".material")).parseMaterial())
                            .withName(config.getString("Gui.items.player-item.name")
                                    .replace("{order}", String.valueOf(number))
                                    .replace("{name}", player.getName()))
                            .withLore(lore).build();
                } else {
                    item = getSkull(number-1, "player-item");
                }
            } else {
                item = new ItemBuilder(XMaterial.valueOf(config.getString("Gui.items.player-not-found.material")).parseMaterial())
                        .withName(config.getString("Gui.items.player-not-found.name"))
                        .withLore(config.getStringList("Gui.items.player-not-found.lore")).build();
            }
            addItem(slot, item);
        }


        addItem(
                config.getInt("Gui.items.close-menu.slot"),
                new Hytem(new ItemBuilder(XMaterial.valueOf(config.getString("Gui.items.close-menu.material")).parseMaterial())
                        .withName(config.getString("Gui.items.close-menu.name"))
                        .withLore(config.getStringList("Gui.items.close-menu.lore")).build(), (event) ->{
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, ()->{
                        event.getWhoClicked().closeInventory();
                    }, 2);
                })
        );

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        if (config.getBoolean("Messages.gui-opened.status"))
            event.getPlayer().sendMessage(Tools.colored(config.getString("Messages.gui-opened.message")));

        Player player = (Player) event.getPlayer();
        int selfOrder = Tools.getOrder(event.getPlayer().getName());
        TopPlayer topPlayer = plugin.getPlayersMap().get(selfOrder);
        List<String> lore = new ArrayList<>();
        for (String str : config.getStringList("Gui.items.self-item.lore")) {
            lore.add(str.replace("{order}", String.valueOf(selfOrder))
                    .replace("{name}", player.getName())
                    .replace("{balance_raw}", String.valueOf(topPlayer.getBalance()))
                    .replace("{balance}", Tools.formatMoney(topPlayer.getBalance())));
        }

        if (config.getBoolean("Gui.custom-item")) {
            addItem(config.getInt("Gui.items.self-item.slot"), new ItemBuilder(
                    XMaterial.matchXMaterial(config.getString("Gui.items.self-item.material")).get().parseItem())
                    .withName(config.getString("Gui.items.self-item.name"))
                    .withLore(lore).build());
        } else {
            addItem(config.getInt("Gui.items.self-item.slot"), getSkull(selfOrder, "self-item"));
        }
    }

    public ItemStack getSkull(Integer number, String path){
        FileConfiguration config = ConfigManager.get("config.yml");

        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        TopPlayer player = plugin.getPlayersMap().get(number);
        meta.setOwner(player.getName());
        meta.setDisplayName(Tools.colored(config.getString("Gui.items."+ path +".name")
                .replace("{order}", String.valueOf(number+1))
                .replace("{name}", player.getName())
                .replace("{balance_raw}", String.valueOf(player.getBalance()))
                .replace("{balance}", Tools.formatMoney(player.getBalance()))));
        List<String> lore = new ArrayList<>();
        for (String str : config.getStringList("Gui.items."+ path +".lore")){
            lore.add(str.replace("{order}", String.valueOf(number+1))
                    .replace("{name}", player.getName())
                    .replace("{balance_raw}", String.valueOf(player.getBalance()))
                    .replace("{balance}", Tools.formatMoney(player.getBalance())));
        }
        meta.setLore(Tools.colored(lore));
        item.setItemMeta(meta);

        return item;
    }

}
