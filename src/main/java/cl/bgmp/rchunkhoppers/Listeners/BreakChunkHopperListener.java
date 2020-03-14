package cl.bgmp.rchunkhoppers.Listeners;

import cl.bgmp.rchoppers.RCHopper;
import cl.bgmp.rchunkhoppers.ChunkHopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakChunkHopperListener implements Listener {
	
	@EventHandler
	public void onChunkHopperBreak(BlockBreakEvent e) {
		try {
			if(!RCHopper.rcHoppersMaps.get(ChunkHopper.class.getName()).containsKey(e.getBlock().getLocation())) return;
		}catch(Exception f) {
		}
	}
}
