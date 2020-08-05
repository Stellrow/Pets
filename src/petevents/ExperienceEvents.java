package petevents;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import main.PetsMain;

public class ExperienceEvents implements Listener{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.getBlock().getType()==Material.STONE||e.getBlock().getType().toString().endsWith("_LOG")) {
		
		Player p = e.getPlayer();
		int toAdd = pl.getConfig().getInt("Exp.blockBreak");
		if(p.hasPermission("loyalpets.booster.2")) {
			toAdd=toAdd*2;
			}
		addXp(p,toAdd);
		}	
	}
	@EventHandler
	public void onKill(EntityDeathEvent e) {
		if(e.getEntity().getKiller()instanceof Player) {
		Player p = e.getEntity().getKiller();
		int toAdd= pl.getConfig().getInt("Exp.mobKilled");
		if(p.hasPermission("loyalpets.booster.2")) {
			toAdd=toAdd*2;
			}
		addXp(p,toAdd);
		}
	}
	
	
	private void addXp(Player p,Integer toAdd) {
		if(pl.playerM.hasCat(p)) {
			pl.playerM.returnCat(p).addXp(toAdd);
			return;
		}
		if(pl.playerM.hasDog(p)) {
			pl.playerM.returnDog(p).addXp(toAdd);
			return;
		}
	}

}
