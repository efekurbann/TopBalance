package xyz.efekurbann.topbalance.utils;

import org.bukkit.ChatColor;
import xyz.efekurbann.topbalance.TopBalancePlugin;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class Tools {

    public static String colored(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> colored(List<String> lore) {
        List<String> list = new ArrayList<>();
        for (String str : lore){
            list.add(colored(str));
        }
        return list;
    }

    public static void reload(){
        ConfigManager.reload("config.yml");
    }

    public static String formatMoney(double money){
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);

        if (money < 1000.0D) {
            return format.format(money);
        } else if (money < 1000000.0D) {
            return format.format(money / 1000.0D) + "k";
        } else if (money < 1.0E9D) {
            return format.format(money / 1000000.0D) + "M";
        } else if (money < 1.0E12D) {
            return format.format(money / 1.0E9D) + "B";
        } else if (money < 1.0E15D) {
            return format.format(money / 1.0E12D) + "T";
        } else {
            return money < 1.0E18D ? format.format(money / 1.0E15D) + "Q" : String.valueOf(money);
        }

    }

    public static int getOrder(String name) {
        AtomicReference<Integer> order = new AtomicReference<>(0);
        TopBalancePlugin.getInstance().getPlayersMap().forEach((key, value) -> {
            if (value.getName().equals(name)) {
                order.set(key);
            }
        });
        return order.get();
    }

}
