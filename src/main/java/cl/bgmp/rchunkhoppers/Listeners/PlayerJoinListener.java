package cl.bgmp.rchunkhoppers.Listeners;

import java.text.DecimalFormat;

import cl.bgmp.rchoppers.RCHoppers;
import cl.bgmp.rchunkhoppers.ChunkHopper;
import cl.bgmp.rchunkhoppers.RCHunkHoppers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		double toPay = 0;
		Player p = e.getPlayer();
		if(ChunkHopper.offlineSold.containsKey(p)) {
			toPay += ChunkHopper.offlineSold.get(p);
			ChunkHopper.offlineSold.remove(p);
		}
		toPay += RCHunkHoppers.getOfflineSold().getDouble(p.getUniqueId().toString(), 0);
		RCHunkHoppers.getOfflineSold().set(p.getUniqueId().toString(), null);
		if(toPay > 0) {
			DecimalFormat df = new DecimalFormat("#.###");
			p.sendMessage(RCHoppers.prefix+ "§aYour Chunk Hoppers have sold $§6" + df.format(toPay) + " §awhile you were offline!");
			RCHunkHoppers.getEconomy().depositPlayer(p, toPay);
		}
	}

}
