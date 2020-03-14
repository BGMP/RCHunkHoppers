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

public class FilterGuiClickListener implements Listener {
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onFilterClick(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) return;
		ItemStack bookItem;
		try {
			if(e.getView().getTitle().contains("selling")) return;
			bookItem = e.getInventory().getItem(9*5+0);
			if(!bookItem.getType().equals(Material.BOOK) || !bookItem.hasItemMeta() || bookItem.getItemMeta().getLocalizedName() == null ||
					!bookItem.getItemMeta().getLocalizedName().startsWith("§1RCH.ChunkHopper.Location.")) return;
		}catch(ArrayIndexOutOfBoundsException f) {
			return;
		}catch(NullPointerException f) {
			return;
		}
		String[] l = bookItem.getItemMeta().getLocalizedName().split("\\.");
		Location loc = new Location(Bukkit.getWorld(l[3]), Double.parseDouble(l[4]), Double.parseDouble(l[5]), Double.parseDouble(l[6]));
		ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(loc);
		if(!loc.equals(ch.getLocation())) {
			e.getWhoClicked().sendMessage("§4Something went wrong: Location error at filterGuiClickListener");
			e.getWhoClicked().sendMessage("§4Please report to server administrator!");
			return;
		}
		
		ItemStack item = e.getCurrentItem().clone();
		e.setCancelled(true);
		if(item == null) return;
		if(!item.hasItemMeta() || item.getItemMeta().getLocalizedName() == null || !item.getItemMeta().getLocalizedName().startsWith("§1RCH.ChunkHopper")) {
			item = new ItemStack(item.getType());
			for(ItemStack i : ch.getFilter()) {
				if(i.getType().equals(item.getType()))
					return;
			}
			for(int j = 0; j < ch.getFilter().length; j++) {
				if(ch.getFilter()[j].getType().equals(Material.AIR)) {
					ch.getFilter()[j] = item;
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
			for(int i = 0; i<ch.getFilter().length; i++) {
				if(ch.getFilter()[i].getType().equals(item.getType())) {
					ch.filter[i] = new ItemStack(Material.AIR);
					ch.filter = SortItemStackArray.sort(ch.getFilter());
					e.getWhoClicked().openInventory(GUI.getFilter(ch, false));
					break;
				}
			}
			break;
			
		case "§1RCH.ChunkHopper.Filter.Blacklist":
			ch.changeWhitelist();
			ItemStack c = e.getCurrentItem();
			c.setType(Material.WHITE_TERRACOTTA);
			ItemMeta m = c.getItemMeta();
			m.setDisplayName("§7Whitelist");
			m.setLore(((List<String>) RCHunkHoppers.getPlugin().getConfig().getList("GUI.Filter.Items.Whitelist.Lore", Arrays.asList("§7Only picks up items set up in this filter","§7Click to change"))));
			m.setLocalizedName("§1RCH.ChunkHopper.Filter.Whitelist");
			c.setItemMeta(m);
			break;
			
		case "§1RCH.ChunkHopper.Filter.Whitelist":
			ch.changeWhitelist();
			ItemStack c2 = e.getCurrentItem();
			c2.setType(Material.BLACK_TERRACOTTA);
			ItemMeta m2 = c2.getItemMeta();
			m2.setDisplayName("§8Blacklist");
			m2.setLore(((List<String>) RCHunkHoppers.getPlugin().getConfig().getList("GUI.Filter.Items.Blacklist.Lore", Arrays.asList("§8Picks up all items but the ones set up in this filter","§8Click to change"))));
			m2.setLocalizedName("§1RCH.ChunkHopper.Filter.Blacklist");
			c2.setItemMeta(m2);
			break;
			
		case "§1RCH.ChunkHopper.Filter.Reset":
			ch.resetFilter("NotSelling");
			for(int i = 0; i < 45; i++) {
				e.getInventory().clear(i);
				e.getView().getTopInventory().setItem(i, new ItemBuilder(Material.getMaterial(RCHoppers.getPlugin().getConfig().getString("DefaultFilters.Normal." + i, "AIR"))).setLocalName("§1RCH.ChunkHopper.Item").build());
			}
			
			break;
		}
	}

}
