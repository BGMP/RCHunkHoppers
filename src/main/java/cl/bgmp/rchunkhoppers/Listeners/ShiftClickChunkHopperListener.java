package cl.bgmp.rchunkhoppers.Listeners;

import cl.bgmp.rchoppers.RCHopper;
import cl.bgmp.rchoppers.RCHoppers;
import cl.bgmp.rchunkhoppers.ChunkHopper;
import cl.bgmp.rchunkhoppers.GUI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShiftClickChunkHopperListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onShiftClick(PlayerInteractEvent e) {
		if(!e.getPlayer().isSneaking() || !e.getClickedBlock().getType().equals(Material.HOPPER)) return;
		if(!RCHopper.rcHoppersMaps.get(ChunkHopper.class.getName()).containsKey(e.getClickedBlock().getLocation())) return;

		ChunkHopper ch = (ChunkHopper) RCHopper.rcHoppersMaps.getOrDefault(ChunkHopper.class.getName(), null).getOrDefault(e.getClickedBlock().getLocation(), null);
		if(ch == null) return;
		if(!ch.exists()) return;
		e.setCancelled(true);
		if(!ch.getPlacer().equals(e.getPlayer().getUniqueId())) {
			if (e.getPlayer().isOp()) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					e.getPlayer().openInventory(GUI.getSettings(false, ch));
				}
			} else {
				e.getPlayer().sendMessage("§cThis is not your §6Chunk Hopper!");
				return;
			}
		} else {
			e.getPlayer().openInventory(GUI.getSettings(false, ch));
		}
		ChunkHopper.settingsGuiOpen.put(e.getPlayer(), ch);
	}

	@EventHandler
	public void onPlayerClickHopper(PlayerInteractEvent event) {
		Block clickedBlock = event.getClickedBlock();
		if (clickedBlock == null) return;

		Material clickedMaterial = clickedBlock.getType();
		if (clickedMaterial != Material.HOPPER) return;

		ChunkHopper chunkHopper = (ChunkHopper) RCHopper.rcHoppersMaps.getOrDefault(ChunkHopper.class.getName(), null).getOrDefault(clickedBlock.getLocation(), null);
		if (chunkHopper == null || !chunkHopper.exists()) return;

		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_BLOCK && !event.getPlayer().isSneaking()) {
			event.setCancelled(true);
			event.getPlayer().openInventory(chunkHopper.getStorage());
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Material blockType = block.getType();
		if (blockType != Material.HOPPER) return;

		ChunkHopper chunkHopper = (ChunkHopper) RCHopper.rcHoppersMaps.getOrDefault(ChunkHopper.class.getName(), null).getOrDefault(block.getLocation(), null);
		if (chunkHopper == null || !chunkHopper.exists()) return;
		event.setCancelled(true);

		Player player = event.getPlayer();
		ChunkHopper.settingsGuiOpen.remove(player);
		chunkHopper.getLocation().getBlock().setType(Material.AIR);
		chunkHopper.remove();

		for (ItemStack itemStack : chunkHopper.getStorage()) {
			if (itemStack != null && itemStack.getType() != Material.AIR) {
				player.getWorld().dropItemNaturally(block.getLocation(), itemStack);
			}
		}

		player.getWorld().dropItemNaturally(block.getLocation(), ChunkHopper.getItem(1));
		player.sendMessage(RCHoppers.prefix + "§cYou have broken your Chunk Hopper!");
	}
}
