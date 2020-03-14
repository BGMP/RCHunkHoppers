package cl.bgmp.rchunkhoppers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cl.bgmp.rchoppers.RCHopper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChunkHopper extends RCHopper {
    private int tier;
    public ItemStack[] filter = new ItemStack[45];
    private boolean whitelist;
    public ItemStack[] sellFilter = new ItemStack[45];
    private boolean sellWhiteList;
    private double sold;

    public static HashMap<Player, ChunkHopper> settingsGuiOpen = new HashMap<Player, ChunkHopper>();
    public static HashMap<OfflinePlayer, Double> offlineSold = new HashMap<OfflinePlayer, Double>();

    private final FileConfiguration pluginConf = RCHunkHoppers.getPlugin().getConfig();

    public ChunkHopper(Location loc, Integer tier, UUID placer) {
        location = loc;
        if(placer == null) {
            setConfigFile(RCHunkHoppers.getPlugin());
            this.tier = config.getInt("Tier", -1);
            this.placer = UUID.fromString(config.getString("Placer", null));
            if(this.placer == null)
                return;
            whitelist = config.getBoolean("Whitelist", false);
            sellWhiteList = config.getBoolean("SellWhitelist", true);
            for(int i = 0; i< filter.length; i++) {
                filter[i] = config.getItemStack("Filter."+i, new ItemStack(Material.AIR));
                sellFilter[i] = config.getItemStack("SellFilter." + i, new ItemStack(Material.AIR));
            }
            sold = config.getDouble("Sold", -1);
            if(config.getString("Friends") != null) {
                Set<String> amount = config.getConfigurationSection("Friends").getKeys(false);
                for(String i : amount) {
                    friends.add(config.getOfflinePlayer("Friends."+Integer.parseInt(i)));
                }
            }
        }else{
            this.tier = 1;
            this.placer = placer;
            whitelist = pluginConf.getBoolean("DefaultFilters.Whitelist.Normal", false);
            sellWhiteList = pluginConf.getBoolean("DefaultFilters.Whitelist.Selling", true);;
            sold = 0;

            for(int i = 0; i< filter.length; i++) {
                filter[i] = new ItemStack(Material.getMaterial(pluginConf.getString("DefaultFilters.Normal." + i, "AIR")));
                sellFilter[i] = new ItemStack(Material.getMaterial(pluginConf.getString("DefaultFilters.Selling." + i, "AIR")));
            }
        }
        register(this, RCHunkHoppers.getPlugin());
    }

    public int getTier() {
        return tier;
    }

    public ItemStack[] getFilter() {
        return filter;
    }

    public boolean getWhitelist() {
        return whitelist;
    }

    public void changeWhitelist() {
        whitelist = !whitelist;
    }

    public ItemStack[] getSellFilter() {
        return sellFilter;
    }

    public boolean getSellWhitelist() {
        return sellWhiteList;
    }

    public void changeSellWhitelist() {
        sellWhiteList = !sellWhiteList;
    }

    public double getSold() {
        return sold;
    }

    public void addToSold(double sold) {
        this.sold += sold;
    }

    public static void addToOfflineSold(double sold, OfflinePlayer player) {
        if(offlineSold.containsKey(player)) {
            offlineSold.put(player, offlineSold.get(player) + sold);
        }else {
            offlineSold.put(player, sold);
        }
    }

    public void saveOfflineSold() {
        for(OfflinePlayer p : offlineSold.keySet()) {
            RCHunkHoppers.getOfflineSold().set(p.getUniqueId().toString(), offlineSold.get(p));
        }
    }

    public static ChunkHopper getChunkHopperInChunk(Location loc) {
        Set<Location> locs = rcHoppersMaps.get(ChunkHopper.class.getName()).keySet();
        for(Location l : locs) {
            if(l.getWorld().equals(loc.getWorld()) && l.getChunk().getX() == loc.getChunk().getX() && l.getChunk().getZ() == loc.getChunk().getZ())
                return (ChunkHopper) rcHoppersMaps.get(ChunkHopper.class.getName()).get(l);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static ItemStack getItem(int amount) {
        ItemStack i = new ItemStack(Material.HOPPER);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(RCHunkHoppers.getPlugin().getConfig().getString("ChunkHopperItem.Name", "§3ChunkHopper"));
        m.setLore((List<String>) RCHunkHoppers.getPlugin().getConfig().getList("ChunkHopperItem.Lore", Arrays.asList("§b§lThis is a Chunk Hopper", "§bIt picks up all the items in a chunk!")));
        m.setLocalizedName("§1ChunkHopper!");
        i.setItemMeta(m);
        i.setAmount(amount);
        return i;
    }

    @SuppressWarnings("unchecked")
    public static ItemStack getUpgradeItem(int amount, int tier) {
        ItemStack i = new ItemStack(Material.IRON_INGOT);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(RCHunkHoppers.getPlugin().getConfig().getString("UpgradeItem.Name", "§aChunk Hopper Upgrade"));
        if(tier == 2) {
            m.setLore((List<String>) RCHunkHoppers.getPlugin().getConfig().getList("UpgradeItem.Lore.1", Arrays.asList("§6Tier 1 §a➜ §6Tier 2")));
        }else {
            m.setLore((List<String>) RCHunkHoppers.getPlugin().getConfig().getList("UpgradeItem.Lore.2", Arrays.asList("§6Tier 2 §a➜ §6Tier 3")));
        }
        m.setLocalizedName("§1ChunkHopperUpgrade!");
        i.setItemMeta(m);
        i.setAmount(amount);
        return i;
    }

    public void upgrade() {
        tier++;
    }

    public void resetFilter(String sell) {
        if(sell == "Selling") {
            for(int i = 0; i< filter.length; i++) {
                sellFilter[i] = new ItemStack(Material.getMaterial(pluginConf.getString("DefaultFilters.Selling." + i, "AIR")));
            }
        }else {
            for(int i = 0; i< filter.length; i++) {
                filter[i] = new ItemStack(Material.getMaterial(pluginConf.getString("DefaultFilters.Normal." + i, "AIR")));
            }
        }
    }
}
