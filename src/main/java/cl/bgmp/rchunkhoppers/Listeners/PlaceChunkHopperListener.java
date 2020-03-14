package cl.bgmp.rchunkhoppers.Listeners;

import cl.bgmp.rchoppers.RCHoppers;
import cl.bgmp.rchunkhoppers.ChunkHopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceChunkHopperListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkHopperPlace(BlockPlaceEvent e) {
		if(!e.getItemInHand().isSimilar(ChunkHopper.getItem(1))) return;
		ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(e.getBlock().getLocation());
		if(ch == null) {
			new ChunkHopper(e.getBlock().getLocation(), 1, e.getPlayer().getUniqueId());
			e.getPlayer().sendMessage(RCHoppers.prefix+ "§aYou have placed down a §3Chunk Hopper!");
		}else {
			e.setCancelled(true);
			e.getPlayer().sendMessage(RCHoppers.prefix+ "§cThere is already a §3Chunk Hopper §cin this chunk!");
		}
	}
}
