package cl.bgmp.rchunkhoppers.Listeners.SettingsGUI;

import cl.bgmp.rchunkhoppers.ChunkHopper;
import cl.bgmp.rchunkhoppers.RCHunkHoppers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseSettingsGuiListener implements Listener {
	
	@EventHandler
	public void onSettingsGuiClose(InventoryCloseEvent e) {
		if(!e.getView().getTitle().equals(RCHunkHoppers.getPlugin().getConfig().getString("GUI.Settings.Name", "3Your Chunk Hopper settings:"))) return;
		if(!ChunkHopper.settingsGuiOpen.containsKey(e.getPlayer())) return;
		ChunkHopper.settingsGuiOpen.remove(e.getPlayer());
//		ItemStack[] cont = e.getView().getTopInventory().getContents();
//		for(ItemStack i : cont) {
//			if(i == null) continue;
//			if(i.isSimilar(ChunkHopper.getUpgradeItem(1)))
//				e.getPlayer().getInventory().addItem(i);
//		}
	}

}
