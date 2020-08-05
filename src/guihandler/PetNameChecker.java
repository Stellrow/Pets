package guihandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import configmanagement.CustomConfig;
import main.PetsMain;
import net.md_5.bungee.api.ChatColor;
import pets.CustomCat;
import pets.CustomDog;

public class PetNameChecker implements Listener{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	private Player p;
	private boolean isChecking = false;
	private CustomConfig cfg;
	private String pettype;
	public PetNameChecker(Player p,CustomConfig cc,String pettype) {
		cfg=cc;
		this.p=p;
		isChecking = true;
		this.pettype=pettype;
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.typeName")));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.typeCancel")));
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	private void end() {
		
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().equals(p)) {
			if(isChecking) {
				e.setCancelled(true);
				if(e.getMessage().equalsIgnoreCase("cancel")) {
					isChecking = false;
					end();
					try {
						this.finalize();
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					return;
				}
				if(pettype.equalsIgnoreCase("dog")) {
					Bukkit.getScheduler().runTask(pl, new Runnable(){

						@Override
						public void run() {
							pl.playerM.addPlayer(p, new CustomDog(p.getWorld(),p,p.getLocation(),e.getMessage(),1,0,0));
						}
						
					
					
				});
					cfg.getCfg().set("Pet.Name", e.getMessage());
					cfg.getCfg().set("Pet.Type", "Dog");
					cfg.getCfg().set("Pet.Level", 1);
					cfg.getCfg().set("Pet.Xp", 0);
					cfg.getCfg().set("Pet.SkillPoints", 0);
					//Skills
					cfg.getCfg().set("Pet.Skills.nutrition", false);
					cfg.getCfg().set("Pet.Skills.haste", false);
					cfg.getCfg().set("Pet.Skills.speed", false);
					cfg.getCfg().set("Pet.Skills.nightvision", false);
					cfg.getCfg().set("Pet.Skills.regen", false);
					cfg.getCfg().set("Pet.Skills.strength", false);
					cfg.getCfg().set("Pet.Skills.autosmelt", false);
					//Save
					cfg.save();
					isChecking = false;
					end();
					try {
						this.finalize();
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					return;
				}
				if(pettype.equalsIgnoreCase("cat")) {
					Bukkit.getScheduler().runTask(pl, new Runnable(){

						@Override
						public void run() {
							pl.playerM.addPlayer(p, new CustomCat(p.getWorld(),p,p.getLocation(),e.getMessage(),1,0,0));
							}
						
					
					
				});
					cfg.getCfg().set("Pet.Name", e.getMessage());
					cfg.getCfg().set("Pet.Type", "Cat");
					cfg.getCfg().set("Pet.Level", 1);
					cfg.getCfg().set("Pet.Xp", 0);
					cfg.getCfg().set("Pet.SkillPoints", 0);
					//Skills
					cfg.getCfg().set("Pet.Skills.nutrition", false);
					cfg.getCfg().set("Pet.Skills.haste", false);
					cfg.getCfg().set("Pet.Skills.speed", false);
					cfg.getCfg().set("Pet.Skills.nightvision", false);
					cfg.getCfg().set("Pet.Skills.regen", false);
					cfg.getCfg().set("Pet.Skills.strength", false);
					cfg.getCfg().set("Pet.Skills.autosmelt", false);
					cfg.save();
					isChecking = false;
					end();
					try {
						this.finalize();
					} catch (Throwable e1) {
						e1.printStackTrace();
					}
					return;
				}
				
				
			}
		}
	}

}
