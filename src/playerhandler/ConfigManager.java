package playerhandler;

import java.util.HashMap;

import org.bukkit.entity.Player;

import configmanagement.CustomConfig;

public class ConfigManager {
	private HashMap<Player,CustomConfig> configs = new HashMap<Player,CustomConfig>();
	
	public void addConfig(Player p,CustomConfig cc) {
		if(!configs.containsKey(p)) {
		configs.put(p, cc);
		}
	}
	public void removeConfig(Player p) {
		if(configs.containsKey(p)) {
		configs.remove(p);
		}
	}
	public boolean isRegistered(Player p) {
		if(configs.containsKey(p)) {
			return true;
		}
	return false;
	}
	public CustomConfig getConfig(Player p) {
		return configs.get(p);
	}
	
	

}
