package guihandler;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class GuiManager {
	private HashMap<Player,GuiHandle> gHandles = new HashMap<Player,GuiHandle>();
	private HashMap<Player,SkillInventory> sInvs = new HashMap<Player,SkillInventory>();
	private HashMap<Player,PetNameChecker> nameC = new HashMap<Player,PetNameChecker>();
	
	public void addGHandle(Player p,GuiHandle gHandle) {
		gHandles.put(p, gHandle);
	}
	public void addSInv(Player p,SkillInventory sInv) {
		sInvs.put(p, sInv);
	}
	public void removeGHandle(Player p) {
		gHandles.remove(p);
	}
	public void removeSInv(Player p) {
		sInvs.remove(p);
	}
	public void addNameC(Player p,PetNameChecker n) {
		nameC.put(p, n);
	}
	public void removeNameC(Player p) {
		nameC.remove(p);
	}

}
