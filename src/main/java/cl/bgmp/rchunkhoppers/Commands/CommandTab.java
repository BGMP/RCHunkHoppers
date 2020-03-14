package cl.bgmp.rchunkhoppers.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandTab implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("gch")) {
			if(args.length == 1) {
				
				List<String> list = new ArrayList<String>();
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.getName().toLowerCase().startsWith(args[0].toLowerCase()) && 
							!p.getName().toLowerCase().equals(args[0].toLowerCase()))
						list.add(p.getName());
				}
				
				return list;
			}
			if(args.length == 2) {
				List<String> list = new ArrayList<String>();
				list.add(""+1);
				list.add(""+64);
				return list;
			}
		}
			
		return new ArrayList<String>();
	}

}
