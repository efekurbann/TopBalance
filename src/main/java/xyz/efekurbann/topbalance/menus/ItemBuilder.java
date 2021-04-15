package xyz.efekurbann.topbalance.menus;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.efekurbann.topbalance.utils.Tools;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount, short durability) {
        this.item = new ItemStack(material, amount, durability);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder withType(Material material) {
        if (this.item == null) {
            this.item = new ItemStack(material);
            return this;
        }
        this.item.setType(material);
        return this;
    }

    public ItemBuilder withName(String name) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(Tools.colored(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withLore(String... lore) {
        return this.withLore(Arrays.asList(lore));
    }

    public ItemBuilder withLore(List<String> lore) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setLore(Tools.colored(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder withDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchant, level, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
