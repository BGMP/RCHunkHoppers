package cl.bgmp.rchunkhoppers;

import java.util.Arrays;
import java.util.List;

import cl.bgmp.rchoppers.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI {
    @SuppressWarnings("unchecked")
    public static Inventory getSettings(boolean friend, ChunkHopper ch) {
        FileConfiguration conf = RCHunkHoppers.getPlugin().getConfig();
        Inventory inv = Bukkit.createInventory(null, 27, conf.getString("GUI.Settings.Name", "§8§nChunk Hopper Controls:"));

        ItemStack upgrade = new ItemBuilder(Material.REPEATER)
                .setName(conf.getString("GUI.Settings.Items.Upgrade.Name",
                        "§a§lUpgrade Chunk Hopper"))
                .setLore(String
                        .join(",",
                                ((List<String>) conf.getList("GUI.Settings.Items.Upgrade.Lore", Arrays.asList("§fUpgrade this Chunk Hopper", "", "§fCost: §c250000"))))
                        .split(","))
                .setLocalName("§1RCH.ChunkHopper.Settings.Upgrade").build();

        ItemStack filter = new ItemBuilder(
                Material.HOPPER)
                .setName(conf.getString("GUI.Settings.Items.Filter.Name", "§a§lFilter Options"))
                .setLore(String
                        .join(",",
                                ((List<String>) conf.getList("GUI.Settings.Items.Filter.Lore",
                                        Arrays.asList("§fChange the item filter", "§ffor this chunk hopper"))))
                        .split(","))
                .setLocalName("§1RCH.ChunkHopper.Settings.Filter").build();

        switch (ch.getTier()) {
            case 1:
                inv.setItem(15, upgrade);
                inv.setItem(11, filter);
                break;

            case 2:
                inv.setItem(15, upgrade);
                inv.setItem(11, filter);
                break;

            case 3:
                inv.setItem(11, filter);

                ItemStack sellFilter = new ItemBuilder(Material.DIAMOND).setName(conf.getString("GUI.Settings.Items.SellingFilter.Name",
                        "§a§lSet selling filter"))
                        .setLore(String.join(",",((List<String>) conf.getList("GUI.Settings.Items.SellingFilter.Lore",
                                Arrays.asList("§3You can set a blacklist or a whitelist")))).split(","))
                        .setLocalName("§1RCH.ChunkHopper.Settings.SellingFilter").build();

                inv.setItem(15, sellFilter);
                break;
        }

        return inv;
    }

    @SuppressWarnings("unchecked")
    public static Inventory getFilter(ChunkHopper h, boolean selling) {
        FileConfiguration conf = RCHunkHoppers.getPlugin().getConfig();
        Inventory inv;
        ItemStack book;
        ItemStack add;
        ItemStack remove;

        ItemStack whitelist;
        ItemStack reset;
        if (selling) {
            inv = Bukkit.createInventory(null, 9 * 6,
                    conf.getString("GUI.SellingFilter.Name", "§3Your Chunk Hopper selling filter:"));

            book = new ItemBuilder(
                    Material.BOOK)
                    .setName(conf.getString("GUI.SellingFilter.Items.Explain.Book.Name",
                            "§3In here you can set a whitelist or a blacklist of max 45 item types."))
                    .setLore(String
                            .join(",",
                                    ((List<String>) conf.getList("GUI.SellingFilter.Items.Explain.Book.Lore",
                                            Arrays.asList("§3Blacklisted items will not get sold",
                                                    "§3In whitelist mode only set items will get sold"))))
                            .split(","))
                    .setLocalName("§1RCH.ChunkHopper.Location." + h.getLocation().getWorld().getName() + "."
                            + h.getLocation().getBlockX() + "." + h.getLocation().getBlockY() + "."
                            + h.getLocation().getBlockZ())
                    .build();

            add = new ItemBuilder(Material.EMERALD_BLOCK)
                    .setName(conf.getString("GUI.SellingFilter.Items.Explain.Emerald.Name",
                            "§2To add an item, click it in your inventory"))
                    .setLore(String.join(",", ((List<String>) conf
                            .getList("GUI.SellingFilter.Items.Explain.Emerald.Lore", Arrays.asList("")))).split(","))
                    .setLocalName("§1RCH.ChunkHopper").build();

            remove = new ItemBuilder(Material.REDSTONE_BLOCK)
                    .setName(conf.getString("GUI.SellingFilter.Items.Explain.Redstone.Name",
                            "§4To remove an item, click it in the filter inventory"))
                    .setLore(String.join(",", ((List<String>) conf
                            .getList("GUI.SellingFilter.Items.Explain.Redstone.Lore", Arrays.asList("")))).split(","))
                    .setLocalName("§1RCH.ChunkHopper").build();

            reset = new ItemBuilder(Material.BARRIER)
                    .setName(conf.getString("GUI.SellingFilter.Items.Reset.Name",
                            "§cTo reset your filter to default click here"))
                    .setLore(String.join(",",
                            ((List<String>) conf.getList("GUI.SellingFilter.Items.Reset.Lore", Arrays.asList(""))))
                            .split(","))
                    .setLocalName("§1RCH.ChunkHopper.Filter.Reset").build();

            if (h.getSellWhitelist()) {
                whitelist = new ItemBuilder(Material.WHITE_TERRACOTTA).setName("§7Whitelist").setLore(String.join(",",
                        ((List<String>) conf.getList("GUI.SellingFilter.Items.Whitelist.Lore",
                                Arrays.asList("§7Only sells items set up in this filter", "§7Click to change"))))
                        .split(",")).setLocalName("§1RCH.ChunkHopper.Filter.Whitelist").build();
            } else {
                whitelist = new ItemBuilder(Material.BLACK_TERRACOTTA).setName("§8Blacklist")
                        .setLore(String.join(",",
                                ((List<String>) conf.getList("GUI.SellingFilter.Items.Blacklist.Lore",
                                        Arrays.asList("§8Sells all items but the ones set up in this filter",
                                                "§8Click to change"))))
                                .split(","))
                        .setLocalName("§1RCH.ChunkHopper.Filter.Blacklist").build();
            }

            for (int i = 0; i < h.getSellFilter().length; i++) {
                if (h.getSellFilter()[i].getType().equals(Material.AIR))
                    continue;
                ItemStack it = h.getSellFilter()[i];
                ItemMeta m = it.getItemMeta();
                m.setLocalizedName("§1RCH.ChunkHopper.Item");
                it.setItemMeta(m);
                inv.setItem(i, it);
            }
        }

        else {
            inv = Bukkit.createInventory(null, 9 * 6, conf.getString("GUI.Filter.Name", "§3Your Chunk Hopper filter:"));

            book = new ItemBuilder(Material.BOOK)
                    .setName(conf.getString("GUI.Filter.Items.Explain.Book.Name",
                            "§3In here you can set a whitelist or a blacklist of max 45 item types."))
                    .setLore(String
                            .join(",",
                                    ((List<String>) conf.getList("GUI.Filter.Items.Explain.Book.Lore",
                                            Arrays.asList("§3Blacklisted items will not get picked up",
                                                    "§3In whitelist mode only set items will get picked up"))))
                            .split(","))
                    .setLocalName("§1RCH.ChunkHopper.Location." + h.getLocation().getWorld().getName() + "."
                            + h.getLocation().getBlockX() + "." + h.getLocation().getBlockY() + "."
                            + h.getLocation().getBlockZ())
                    .build();

            add = new ItemBuilder(Material.EMERALD_BLOCK)
                    .setName(conf.getString("GUI.Filter.Items.Explain.Emerald.Name",
                            "§2To add an item, click it in your inventory"))
                    .setLore(String.join(",",
                            ((List<String>) conf.getList("GUI.Filter.Items.Explain.Emerald.Lore", Arrays.asList(""))))
                            .split(","))
                    .setLocalName("§1RCH.ChunkHopper").build();

            remove = new ItemBuilder(Material.REDSTONE_BLOCK)
                    .setName(conf.getString("GUI.Filter.Items.Explain.Redstone.Name",
                            "§4To remove an item, click it in the filter inventory"))
                    .setLore(String.join(",",
                            ((List<String>) conf.getList("GUI.Filter.Items.Explain.Redstone.Lore", Arrays.asList(""))))
                            .split(","))
                    .setLocalName("§1RCH.ChunkHopper").build();

            reset = new ItemBuilder(Material.BARRIER)
                    .setName(conf.getString("GUI.Filter.Items.Reset.Name",
                            "§cTo reset your filter to default click here"))
                    .setLore(String
                            .join(",", ((List<String>) conf.getList("GUI.Filter.Items.Reset.Lore", Arrays.asList(""))))
                            .split(","))
                    .setLocalName("§1RCH.ChunkHopper.Filter.Reset").build();

            if (h.getWhitelist()) {
                whitelist = new ItemBuilder(Material.WHITE_TERRACOTTA).setName("§7Whitelist").setLore(String.join(",",
                        ((List<String>) conf.getList("GUI.Filter.Items.Whitelist.Lore",
                                Arrays.asList("§7Only picks up items set up in this filter", "§7Click to change"))))
                        .split(",")).setLocalName("§1RCH.ChunkHopper.Filter.Whitelist").build();
            } else {
                whitelist = new ItemBuilder(Material.BLACK_TERRACOTTA).setName("§8Blacklist")
                        .setLore(String.join(",",
                                ((List<String>) conf.getList("GUI.Filter.Items.Blacklist.Lore",
                                        Arrays.asList("§8Picks up all items but the ones set up in this filter",
                                                "§8Click to change"))))
                                .split(","))
                        .setLocalName("§1RCH.ChunkHopper.Filter.Blacklist").build();
            }

            for (int i = 0; i < h.getFilter().length; i++) {
                if (h.getFilter()[i].getType().equals(Material.AIR))
                    continue;
                ItemStack it = h.getFilter()[i];
                ItemMeta m = it.getItemMeta();
                m.setLocalizedName("§1RCH.ChunkHopper.Item");
                it.setItemMeta(m);
                inv.setItem(i, it);
            }
        }

        inv.setItem(9 * 5 + 0, book);
        inv.setItem(9 * 5 + 2, add);
        inv.setItem(9 * 5 + 4, remove);
        inv.setItem(9 * 5 + 6, whitelist);
        inv.setItem(9 * 5 + 8, reset);
        return inv;
    }
}
