package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import guihandler.GuiHandle;
import main.PetsMain;

public class PetsCommands implements CommandExecutor{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	private String prefix = ChatColor.GOLD+"[LoyalPets] ";
	@Override
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			//Admin
			//Rename
			if(args.length>=4&&args[0].equalsIgnoreCase("admin")){
				if(!p.hasPermission("loyalpets.admin")) {
					p.sendMessage(prefix+ChatColor.RED+"No permission to do this");
					return true;
				}
				if(args[1].equalsIgnoreCase("rename")) {
					Player target = Bukkit.getPlayer(args[2]);
					if(target==null) {
						p.sendMessage(prefix+ChatColor.RED+"Target player offline or not found");
						return true;
					}
					if(pl.playerM.hasCat(p)) {
						pl.playerM.returnCat(p).rename(args[3]);
						p.sendMessage(prefix+ChatColor.GREEN+"Target's pet succesfully renamed");
						return true;
					}
					if(pl.playerM.hasDog(p)) {
						pl.playerM.returnDog(p).rename(args[3]);
						p.sendMessage(prefix+ChatColor.GREEN+"Target's pet succesfully renamed");
						return true;
					}
				}
			}
			if(args.length>=3&&args[0].equalsIgnoreCase("admin")) {
				if(!p.hasPermission("loyalpets.admin")) {
					p.sendMessage(prefix+ChatColor.RED+"No permission to do this");
					return true;
				}
				if(args[1].equalsIgnoreCase("delete")) {
					Player target = Bukkit.getPlayer(args[2]);
					if(target==null) {
						p.sendMessage(prefix+ChatColor.RED+"Target player offline or not found");
						return true;
					}
					pl.playerM.removePlayer(target);
					pl.configM.getConfig(target).getCfg().set("Pet", null);
					pl.configM.getConfig(target).save();
					p.sendMessage(prefix+ChatColor.GREEN+"Target's pet succesfully deleted");
					return true;
				}
			}
			
			
			
			
			
			
			
			if(p.hasPermission("loyalpets.use")) {
				@SuppressWarnings("unused")
			GuiHandle gh = new GuiHandle(p,pl.configM.getConfig(p));
				return true;
			}
		}
		return true;
	}

}
