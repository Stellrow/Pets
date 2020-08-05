package guihandler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import configmanagement.CustomConfig;
import main.PetsMain;

public class SkillInventory implements Listener{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	private Inventory inv = Bukkit.createInventory(null, 27,ChatColor.GREEN+"Skills");
	private String unlocked = ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("SkillsInventory.unlocked"));
	private String statusSkill =  ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("SkillsInventory.statusSkill"));
	private String skillBought = ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.skillBought"));
	private String notEnoughPoints = ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messages.notEnoughPoints"));
	private CustomConfig cfg;
	private ItemStack speed,haste,nutrition,nightvision,regen,strength,autosmelt;
	private int skillPoints;
	
	public SkillInventory(Player p,CustomConfig cfg) {
		this.cfg=cfg;
		skillPoints=cfg.getCfg().getInt("Pet.SkillPoints");
		buildInventory();
		p.openInventory(inv);
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	private void buildInventory() {
		
		speed = buildItem("speed");
		haste = buildItem("haste");
		nutrition = buildItem("nutrition");
		nightvision = buildItem("nightvision");
		regen = buildItem("regen");
		strength = buildItem("strength");
		autosmelt = buildItem("autosmelt");
		
		inv.setItem(0, buildDisplay(Material.BOOKSHELF));
		inv.setItem(10, speed);
		inv.setItem(11, haste);
		inv.setItem(12, nightvision);
		inv.setItem(13, regen);
		inv.setItem(14, strength);
		inv.setItem(15, nutrition);
		inv.setItem(16, autosmelt);
		
		
	}
	private ItemStack buildDisplay(Material mat) {
		ItemStack i = new ItemStack(mat);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"SkillPoints: "+ChatColor.RED+skillPoints);
		i.setItemMeta(im);
		return i;
	}
	private ItemStack buildItem(String path) {
		ItemStack i = new ItemStack(Material.valueOf(pl.getGui().getString("SkillsInventory."+path+".Type")));
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("SkillsInventory."+path+".Name")));
		im.setLore(retLore(pl.getGui().getStringList("SkillsInventory."+path+".Lore"),path));
		i.setItemMeta(im);
		return i;
	}
	//Check skill
	private boolean checkSkill(String path) {
		return cfg.getCfg().getBoolean("Pet.Skills."+path);
	}
	private int getSkill(String skill) {
		return pl.getConfig().getInt("SkillsCost."+skill);
	}
	
	//Get lore
		private List<String> retLore(List<String>lore,String skill){
			List<String> toRet = new ArrayList<String>();
			if(!checkSkill(skill)) {
				for(String s : lore) {
					toRet.add(ChatColor.translateAlternateColorCodes('&', s).replaceAll("%statusSkill", statusSkill).replaceAll("%skillCost", getSkill(skill)+""));
				}
			}else {
				for(String s : lore) {
					toRet.add(ChatColor.translateAlternateColorCodes('&', s).replaceAll("%statusSkill", unlocked));
				}
			}
			
			return toRet;
		}
		private void end() {
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		private void setPoints(Player p,Integer a) {
			if(pl.playerM.hasDog(p)) {
				pl.playerM.returnDog(p).decreaseSkillPoints(a);
			}
			if(pl.playerM.hasCat(p)) {
				pl.playerM.returnCat(p).decreaseSkillPoints(a);
			}
		}
		//Events
		@EventHandler
		public void onClick(InventoryClickEvent e) {
			if(e.getClickedInventory()==null) {
				return;
			}
			Player p = (Player) e.getWhoClicked();
			if(e.getClickedInventory().equals(inv)) {
				
				e.setCancelled(true);
			
			if(e.getCurrentItem()==null) {
				return;
			}
			
			if(e.getCurrentItem().equals(speed)) {
				if(checkSkill("speed")) {
					return;}
					//Unlock
					if(skillPoints>=pl.getConfig().getInt("SkillsCost.speed")) {
						setPoints(p,pl.getConfig().getInt("SkillsCost.speed"));
						p.sendMessage(skillBought);
						enableSkill("speed");
						e.getWhoClicked().closeInventory();
						end();
					}else {
						p.sendMessage(notEnoughPoints);
						p.closeInventory();
						end();
						
					}
						}
			
			
			if(e.getCurrentItem().equals(haste)) {
				if(checkSkill("haste")) {
					return;}
					//Unlock
					if(skillPoints>=pl.getConfig().getInt("SkillsCost.haste")) {
						setPoints(p,pl.getConfig().getInt("SkillsCost.haste"));
						p.sendMessage(skillBought);
						enableSkill("haste");
						e.getWhoClicked().closeInventory();
						end();
					}else {
						p.sendMessage(notEnoughPoints);
						p.closeInventory();
						end();
						return;
					}
					}
			
			
			if(e.getCurrentItem().equals(nightvision)) {
				if(checkSkill("nightvision")) {
					return;}
					//Unlock
					if(skillPoints>=pl.getConfig().getInt("SkillsCost.nightvision")) {
						setPoints(p,pl.getConfig().getInt("SkillsCost.nightvision"));
						enableSkill("nightvision");
						p.sendMessage(skillBought);
						e.getWhoClicked().closeInventory();
						end();
					}else {
						p.sendMessage(notEnoughPoints);
						p.closeInventory();
						end();
						return;
					}
					}
			
			
			if(e.getCurrentItem().equals(regen)) {
				if(checkSkill("regen")) {
					return;}
					//Unlock
					if(skillPoints>=pl.getConfig().getInt("SkillsCost.regen")) {
						setPoints(p,pl.getConfig().getInt("SkillsCost.regen"));
						p.sendMessage(skillBought);
						enableSkill("regen");
						e.getWhoClicked().closeInventory();
						end();
					}else {
						p.sendMessage(notEnoughPoints);
						p.closeInventory();
						end();
						return;
					}
					}
			
			
			if(e.getCurrentItem().equals(strength)) {
				if(checkSkill("strength")) {
					return;}
					//Unlock
					if(skillPoints>=pl.getConfig().getInt("SkillsCost.strength")) {
						setPoints(p,pl.getConfig().getInt("SkillsCost.strength"));
						p.sendMessage(skillBought);
						enableSkill("strength");
						e.getWhoClicked().closeInventory();
						end();
					}else {
						p.sendMessage(notEnoughPoints);
						p.closeInventory();
						end();
						return;
					}
					}
			
			
			if(e.getCurrentItem().equals(nutrition)) {
				if(checkSkill("nutrition")) {
					return;}
					//Unlock
					if(skillPoints>=pl.getConfig().getInt("SkillsCost.nutrition")) {
						setPoints(p,pl.getConfig().getInt("SkillsCost.nutrition"));
						p.sendMessage(skillBought);
						enableSkill("nutrition");
						e.getWhoClicked().closeInventory();
						end();
					}else {
						p.sendMessage(notEnoughPoints);
						p.closeInventory();
						end();
						return;
					}
					}
			
			
			
			if(e.getCurrentItem().equals(autosmelt)) {
				if(checkSkill("autosmelt")) {
					return;
					}
					//Unlock
					if(skillPoints>=pl.getConfig().getInt("SkillsCost.autosmelt")) {
						setPoints(p,pl.getConfig().getInt("SkillsCost.autosmelt"));
						p.sendMessage(skillBought);
						enableSkill("autosmelt");
						e.getWhoClicked().closeInventory();
						end();
					}else {
						p.sendMessage(notEnoughPoints);
						p.closeInventory();
						end();
						return;
					}
					}
			
			return;
		}}
		public void enableSkill(String bought) {
			cfg.getCfg().set("Pet.Skills."+bought, true);
			cfg.save();
		}
		@EventHandler
		public void onClose(InventoryCloseEvent e) {
			if(e.getInventory().equals(inv)) {
				try {
					this.finalize();
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
		}

}
