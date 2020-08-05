package playerhandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import guihandler.GuiHandle;
import guihandler.PetNameChecker;
import guihandler.RenamePetHandle;
import guihandler.SkillInventory;
import main.PetsMain;

public class PHandle {
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	private SkillInventory skillinv;
	private GuiHandle handler;
	private PetNameChecker checker;
	private RenamePetHandle renameH;
	private Player p;
	
	public PHandle(Player p) {
		this.p=p;
	}
	
	public void nullEverything() {
		nullHandle();
		nullChecker();
		nullSInv();
	}
	public Player getP() {
		return p;
	}
	public SkillInventory getSkillinv() {
		return skillinv;
	}
	public void setSkillinv(SkillInventory skillinv) {
		this.skillinv = skillinv;
	}
	public void nullHandle() {
		new BukkitRunnable() {

			@Override
			public void run() {
				handler=null;
				
			}
			
		}.runTaskLater(pl, 10);
		
	}
	public void nullChecker() {
			new BukkitRunnable() {

				@Override
				public void run() {
					checker=null;
					
				}
				
			}.runTaskLater(pl, 10);
			
		}
	public void nullSInv() {
		new BukkitRunnable() {

			@Override
			public void run() {
				skillinv=null;
				
			}
			
		}.runTaskLater(pl, 10);
		
	}

	public GuiHandle getHandler() {
		return handler;
	}
	public void setHandler(GuiHandle handler) {
		this.handler = handler;
	}
	public PetNameChecker getChecker() {
		return checker;
	}
	public void setChecker(PetNameChecker checker) {
		this.checker = checker;
	}

	public RenamePetHandle getRenameH() {
		return renameH;
	}

	public void setRenameH(RenamePetHandle renameH) {
		this.renameH = renameH;
	}

}
