package cl.bgmp.rchunkhoppers.Listeners;

import java.util.HashMap;

import cl.bgmp.rchunkhoppers.ChunkHopper;
import cl.bgmp.rchunkhoppers.RCHunkHoppers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class PickupItemListenerNormal implements Listener {

	@EventHandler
	public void onItemDrop(ItemSpawnEvent e) {
		ItemStack droppedItem = e.getEntity().getItemStack();
		ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(e.getLocation());
		if (ch == null)
			return;
		
		boolean inSellFilter = false;
		if(ch.getTier() == 3) {
			for (ItemStack i : ch.getSellFilter()) {
				if (i.getType().equals(droppedItem.getType())) {
					inSellFilter = true;
					break;
				}
			}
			inSellFilter = ch.getSellWhitelist() ? inSellFilter : !inSellFilter;
		}

		if (inSellFilter) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(ch.getPlacer());
			double price = RCHunkHoppers.getWorth().getDouble("Worth." + droppedItem.getType().name(), -1);
			if(price > 0) {
				price *= droppedItem.getAmount();
				ch.addToSold(price);
				e.setCancelled(true);
				if(player.isOnline()) {
					RCHunkHoppers.getEconomy().depositPlayer(player, price);
				}else {
					ChunkHopper.addToOfflineSold(price, player);
				}return;
			}
		}
		boolean inFilter = false;

		if(ch.getTier() == 1) {
			inFilter = true;
		}else {
			for (ItemStack i : ch.getFilter()) {
				if (i.getType().equals(droppedItem.getType())) {
					inFilter = true;
					break;
				}
			}
			inFilter = ch.getWhitelist() ? inFilter : !inFilter;
		}

		if (!inFilter)
			return;
		int chestNumber = 0;
		Location chestLoc = ch.getLocation().clone();
		for (int i = 1; i < 255; i++) {
			Material m = chestLoc.add(0, -1, 0).getBlock().getType();
			if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST) || m.equals(Material.getMaterial("BARREL"))
					|| m.equals(Material.getMaterial("SHULKER_BOX")))
				chestNumber++;
			else
				break;
		}
		chestLoc.add(0, 1, 0);

		Container cont = null;
		for (int i = chestNumber; i >= 0; i--) {
			cont = (Container) chestLoc.getBlock().getState();
			HashMap<Integer, ItemStack> remaining = ch.getStorage().addItem(droppedItem);
			if (remaining.isEmpty()) {
				e.setCancelled(true);
				break;
			}
			droppedItem = remaining.get(0);
			if (i == chestNumber) {
				e.getEntity().getItemStack().setAmount(droppedItem.getAmount());
			}
			chestLoc.add(0, 1, 0);
		}

	}

//		boolean inFilter = false;
//		for(ItemStack i : ch.getFilter()) {
//			if(i.getType().equals(droppedItem.getType())) {
//				inFilter = true;
//				break;
//			}
//		}
//		inFilter = ch.getWhitelist() ? inFilter : !inFilter;
//		if(!inFilter) return;
//		int chestNumber = 0;
//		Location chestLoc = ch.getLocation().clone();
//		for(int i = 1; i<255; i++) {
//			Material m = chestLoc.add(0,-1,0).getBlock().getType();
//			if(m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST) || m.equals(Material.BARREL) || m.equals(Material.SHULKER_BOX)) chestNumber++;
//			else break;
//		}
//		chestLoc.add(0,1,0);
//		
//		Container cont = null;
//		for(int i = chestNumber; i>=0; i--) {
//			cont = (Container) chestLoc.getBlock().getState();
//			HashMap<Integer, ItemStack> remaining = cont.getInventory().addItem(droppedItem);
//			if(remaining.isEmpty()) {
//				e.setCancelled(true);
//				break;
//			}
//			droppedItem = remaining.get(0);
//			if(i == chestNumber) {
//				e.getEntity().getItemStack().setAmount(droppedItem.getAmount());
//			}
//			chestLoc.add(0,1,0);
//		}
//	}

//	private void putInside(ChunkHopper ch, ItemStack droppedItem, ItemSpawnEvent e) {
//		boolean inFilter = false;
//		for (ItemStack i : ch.getFilter()) {
//			if (i.getType().equals(droppedItem.getType())) {
//				inFilter = true;
//				break;
//			}
//		}
//		inFilter = ch.getWhitelist() ? inFilter : !inFilter;
//		if (!inFilter)
//			return;
//		int chestNumber = 0;
//		Location chestLoc = ch.getLocation().clone();
//		for (int i = 1; i < 255; i++) {
//			Material m = chestLoc.add(0, -1, 0).getBlock().getType();
//			if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST) || m.equals(Material.BARREL)
//					|| m.equals(Material.SHULKER_BOX))
//				chestNumber++;
//			else
//				break;
//		}
//		chestLoc.add(0, 1, 0);
//
//		Container cont = null;
//		for (int i = chestNumber; i >= 0; i--) {
//			cont = (Container) chestLoc.getBlock().getState();
//			HashMap<Integer, ItemStack> remaining = cont.getInventory().addItem(droppedItem);
//			if (remaining.isEmpty()) {
//				e.setCancelled(true);
//				break;
//			}
//			droppedItem = remaining.get(0);
//			if (i == chestNumber) {
//				e.getEntity().getItemStack().setAmount(droppedItem.getAmount());
//			}
//			chestLoc.add(0, 1, 0);
//		}
//	}

}
