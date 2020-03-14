package cl.bgmp.rchunkhoppers.Listeners.FilterGUI;

import java.util.Arrays;
import java.util.List;

import cl.bgmp.rchoppers.ItemBuilder;
import cl.bgmp.rchoppers.RCHoppers;
import cl.bgmp.rchunkhoppers.ChunkHopper;
import cl.bgmp.rchunkhoppers.GUI;
import cl.bgmp.rchunkhoppers.RCHunkHoppers;
import cl.bgmp.rchunkhoppers.SortItemStackArray;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SellFilterGuiClickListener implements Listener{

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onSellFilterClick(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) return;
		ItemStack bookItem;
		try {
			if(!e.getView().getTitle().toLowerCase().contains("selling".toLowerCase())) return;
			bookItem = e.getInventory().getItem(9*5+0);
		}catch(ArrayIndexOutOfBoundsException f) {
			return;
		}catch(NullPointerException f) {
			return;
		}
		String[] l = bookItem.getItemMeta().getLocalizedName().split("\\.");
		Location loc = new Location(Bukkit.getWorld(l[3]), Double.parseDouble(l[4]), Double.parseDouble(l[5]), Double.parseDouble(l[6]));
		ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(loc);
		if(!loc.equals(ch.getLocation())) {
			e.getWhoClicked().sendMessage("§4Something went wrong: Location error at sellFilterGuiClickListener");
			e.getWhoClicked().sendMessage("§4Please report to server administrator!");
			return;
		}
		
		ItemStack item = e.getCurrentItem().clone();
		e.setCancelled(true);
		if(item == null) return;
		if(!item.hasItemMeta() || item.getItemMeta().getLocalizedName() == null || !item.getItemMeta().getLocalizedName().startsWith("§1RCH.ChunkHopper")) {
			item = new ItemStack(item.getType());
			for(ItemStack i : ch.getSellFilter()) {
				if(i.getType().equals(item.getType()))
					return;
			}
			for(int j = 0; j < ch.getSellFilter().length; j++) {
				if(ch.getSellFilter()[j].getType().equals(Material.AIR)) {
					ch.getSellFilter()[j] = item;
					ItemMeta m = item.getItemMeta();
					m.setLocalizedName("§1RCH.ChunkHopper.Item");
					item.setItemMeta(m);
					e.getView().getTopInventory().addItem(item);
					return;
				}
			}
			return;
		}
		
		switch (item.getItemMeta().getLocalizedName()) {
		case "§1RCH.ChunkHopper.Item":
			for(int i = 0; i<ch.getSellFilter().length; i++) {
				if(ch.getSellFilter()[i].getType().equals(item.getType())) {
					ch.sellFilter[i] = new ItemStack(Material.AIR);
					ch.sellFilter = SortItemStackArray.sort(ch.getSellFilter());
					e.getWhoClicked().openInventory(GUI.getFilter(ch, true));
					break;
				}
			}
			break;
			
		case "§1RCH.ChunkHopper.Filter.Blacklist":
			ch.changeSellWhitelist();
			ItemStack c = e.getCurrentItem();
			c.setType(Material.WHITE_TERRACOTTA);
			ItemMeta m = c.getItemMeta();
			m.setDisplayName("§7Whitelist");
			m.setLore(((List<String>) RCHunkHoppers.getPlugin().getConfig().getList("GUI.SellingFilter.Items.Whitelist.Lore", Arrays.asList("§7Only picks up items set up in this filter","§7Click to change"))));
			m.setLocalizedName("§1RCH.ChunkHopper.Filter.Whitelist");
			c.setItemMeta(m);
			break;
			
		case "§1RCH.ChunkHopper.Filter.Whitelist":
			ch.changeSellWhitelist();
			ItemStack c2 = e.getCurrentItem();
			c2.setType(Material.BLACK_TERRACOTTA);
			ItemMeta m2 = c2.getItemMeta();
			m2.setDisplayName("§8Blacklist");
			m2.setLore(((List<String>) RCHunkHoppers.getPlugin().getConfig().getList("GUI.SellingFilter.Items.Blacklist.Lore", Arrays.asList("§8Picks up all items but the ones set up in this filter","§8Click to change"))));
			m2.setLocalizedName("§1RCH.ChunkHopper.Filter.Blacklist");
			c2.setItemMeta(m2);
			break;
			
		case "§1RCH.ChunkHopper.Filter.Reset":
			ch.resetFilter("Selling");
			for(int i = 0; i < 45; i++) {
				e.getView().getTopInventory().clear(i);
				e.getView().getTopInventory().setItem(i, new ItemBuilder(Material.getMaterial(RCHoppers.getPlugin().getConfig().getString("DefaultFilters.Selling." + i, "AIR"))).setLocalName("§1RCH.ChunkHopper.Item").build());
			}
			break;

		default:
			e.getWhoClicked().sendMessage("§cSomething went wrong: FilterGuiClickListener switch default!");
			e.getWhoClicked().sendMessage("§cPlease report!");
			break;
		}
	}

}
