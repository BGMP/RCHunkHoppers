package cl.bgmp.rchunkhoppers.Listeners.SettingsGUI;

import cl.bgmp.rchoppers.RCHoppers;
import cl.bgmp.rchunkhoppers.ChunkHopper;
import cl.bgmp.rchunkhoppers.GUI;
import cl.bgmp.rchunkhoppers.RCHunkHoppers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SettingsGuiClickListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSettingsGuiClick(InventoryClickEvent e) {
		if(!e.getView().getTitle().equals(RCHunkHoppers.getPlugin().getConfig().getString("GUI.Settings.Name", "§3Your Chunk Hopper settings:"))) return;
		if(!ChunkHopper.settingsGuiOpen.containsKey(e.getWhoClicked())) return;
		if(!e.getCurrentItem().isSimilar(ChunkHopper.getUpgradeItem(1, 2)) && !e.getCursor().isSimilar(ChunkHopper.getUpgradeItem(1,2)) &&
				!e.getCurrentItem().isSimilar(ChunkHopper.getUpgradeItem(1, 3)) && !e.getCursor().isSimilar(ChunkHopper.getUpgradeItem(1,3)))
			e.setCancelled(true);
		
		if(!e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getLocalizedName() == null) return;
		ChunkHopper ch = ChunkHopper.settingsGuiOpen.get(e.getWhoClicked());
		
		switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {
			
		case "§1RCH.ChunkHopper.Settings.Filter":
			e.getWhoClicked().openInventory(GUI.getFilter(ch,false));
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			break;
			
		case "§1RCH.ChunkHopper.Settings.SellingFilter":
			e.getWhoClicked().openInventory(GUI.getFilter(ch,true));
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			break;
			
		case "§1RCH.ChunkHopper.Settings.Upgrade":
			Player player = (Player) e.getWhoClicked();
			final double balance = RCHunkHoppers.getEconomy().getBalance(player);
			if (balance >= 250000) {
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().sendMessage(RCHoppers.prefix + "§aYou have upgraded your §6Chunk Hopper §ato §clevel "+(ch.getTier()+1) +"§a!");
				ch.upgrade();
				ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
				RCHunkHoppers.getEconomy().withdrawPlayer(player, 250000);
			} else {
				e.getWhoClicked().sendMessage(RCHoppers.prefix + "§cYou do not have enough money!");
				player.closeInventory();
			}
		}
	}
}
