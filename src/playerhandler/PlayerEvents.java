package playerhandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import configmanagement.CustomConfig;
import main.PetsMain;

public class PlayerEvents implements Listener{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(!pl.configM.isRegistered(p)) {
			pl.configM.addConfig(p, new CustomConfig(p.getUniqueId().toString()));
		}
		pl.playerM.tryFindPlayer(p, pl.configM.getConfig(p));
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(pl.configM.isRegistered(p)) {
			pl.configM.removeConfig(p);
		}
		if(pl.playerM.isRegistered(p)) {
			pl.playerM.removePlayer(p);
		}
	}

}
