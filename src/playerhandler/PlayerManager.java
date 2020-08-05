package playerhandler;

import java.util.HashMap;

import org.bukkit.entity.Player;

import configmanagement.CustomConfig;
import pets.CustomCat;
import pets.CustomDog;

public class PlayerManager {
	private HashMap<Player,CustomDog> listaCaini = new HashMap<Player,CustomDog>();
	private HashMap<Player,CustomCat> listaPisici = new HashMap<Player,CustomCat>();
	private HashMap<Player,PHandle> pmanager = new HashMap<Player,PHandle>();
	
	
	public void managePlayer(Player p) {
		if(pmanager.containsKey(p)) {
			return;
		}
		pmanager.put(p, new PHandle(p));
	}
	public PHandle getHandle(Player p) {
		return pmanager.get(p);
	}
	public void deleteManage(Player p) {
		pmanager.get(p).nullChecker();
	}
	public void addPlayer(Player p,CustomDog dog) {
		if(!listaCaini.containsKey(p)) {
		listaCaini.put(p, dog);
		}
	}
	public void addPlayer(Player p,CustomCat cat) {
		if(!listaCaini.containsKey(p)) {
		listaPisici.put(p, cat);
		}
	}
	public CustomDog returnDog(Player p) {
		if(listaCaini.containsKey(p)) {
			return listaCaini.get(p);
		}
		return null;
	}
	public CustomCat returnCat(Player p) {
		if(listaPisici.containsKey(p)) {
			return listaPisici.get(p);
		}
		return null;
	}
	public boolean hasCat(Player p) {
		if(listaPisici.containsKey(p)) {
			return true;
		}
		return false;
	}
	public boolean hasDog(Player p) {
		if(listaCaini.containsKey(p)) {
			return true;
		}
		return false;
	}
	public void removePlayer(Player p) {
		if(listaCaini.containsKey(p)) {
			listaCaini.get(p).kill();
			listaCaini.remove(p);
		}
		if(listaPisici.containsKey(p)) {
			listaPisici.get(p).kill();
			listaPisici.remove(p);
		}
	}
	public boolean isRegistered(Player p) {
		if(listaCaini.containsKey(p)) {
			return true;
		}
		if(listaPisici.containsKey(p)) {
			return true;
		}
		return false;
	}
	public void cleanUp() {
		for(Player p : listaCaini.keySet()) {
			listaCaini.get(p).kill();
		}
		for(Player p : listaPisici.keySet()) {
			listaPisici.get(p).kill();
		}
	}
	public void tryFindPlayer(Player p,CustomConfig cfg) {
		if(cfg.getCfg().contains("Pet")) {
		String tip = cfg.getCfg().getString("Pet.Type");
		String nume = cfg.getCfg().getString("Pet.Name");
		int lvl = cfg.getCfg().getInt("Pet.Level");
		int xp = cfg.getCfg().getInt("Pet.Xp");
		int skillPoints = cfg.getCfg().getInt("Pet.SkillPoints");
		if(tip.equalsIgnoreCase("dog")) {
			listaCaini.put(p, new CustomDog(p.getWorld(),p,p.getLocation(),nume,lvl,xp,skillPoints));
			return;
		}
		if(tip.equalsIgnoreCase("cat")) {
			listaPisici.put(p, new CustomCat(p.getWorld(),p,p.getLocation(),nume,lvl,xp,skillPoints));
			return;
		}
		}
	}
}
