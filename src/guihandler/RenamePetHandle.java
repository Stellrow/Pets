package guihandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import main.PetsMain;
import net.md_5.bungee.api.ChatColor;

public class RenamePetHandle implements Listener{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	private Player p;
	private boolean isChecking = false;
	public RenamePetHandle(Player p) {
		this.p=p;
		isChecking=true;
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.typeName")));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.typeCancel")));
		pl.getServer().getPluginManager().registerEvents(this, pl);
	
	}
	private void end() {
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().equals(p)) {
			if(isChecking) {
				e.setCancelled(true);
				if(e.getMessage().equalsIgnoreCase("cancel")) {
					isChecking=false;
					end();
					return;
				}
				String numeNou = e.getMessage();
				if(pl.playerM.hasCat(p)) {
					pl.playerM.returnCat(p).rename(numeNou);
					isChecking=false;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.petRename")));
					end();
					return;
				}
				if(pl.playerM.hasDog(p)) {
					pl.playerM.returnDog(p).rename(numeNou);
					isChecking=false;
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.petRename")));
					end();
					return;
				}
				
			}
		}
	}

}
