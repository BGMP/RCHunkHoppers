package cl.bgmp.rchunkhoppers.Commands;

import cl.bgmp.rchunkhoppers.ChunkHopper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetChunkHopperCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!sender.isOp() && !sender.hasPermission("chunkhoppers.admin")) {
			sender.sendMessage("§4You don't have permission to use that command!");
			return false;
		}
		if(args.length != 2) {
			sender.sendMessage("§4Please use §c/gch <player name> <amount>");
		}
		Player p = Bukkit.getPlayer(args[0]);
		if(p == null) {
			sender.sendMessage("§cA player with that name was not found!");
			return false;
		}
		try {
			p.getInventory().addItem(ChunkHopper.getItem(Integer.parseInt(args[1])));
		}catch(NumberFormatException e) {
			sender.sendMessage("§c"+ args[1] + " is not a number!");
			return false;
		}
		return true;
			
	}

}
