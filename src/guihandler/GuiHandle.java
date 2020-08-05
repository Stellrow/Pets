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

public class GuiHandle implements Listener{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	//Internal pet values
	private String nume;
	private Integer lvl;
	private Integer xp;
	private Integer skillPoints;
	private Integer multiplier = pl.getConfig().getInt("LevelUp.levelMultiply");
	//Inventory
	private Inventory mainPage = Bukkit.createInventory(null, 27,ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("MainPage.InventoryName")));
	private Inventory petInventory; 
	//ItemStacks mainpage
	ItemStack cat,dog;
	//ItemStack pet inventory
	ItemStack barrier,skills,rename;
	//Passable vars
	private CustomConfig cfg;
	private Player p;
	
	public GuiHandle(Player pp,CustomConfig config) {
		this.cfg=config;
		this.p= pp;
		openMenu(p);
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	private void openMenu(Player p) {
		if(cfg.getCfg().contains("Pet.Name")) {
			loadValues();
			petInventory = Bukkit.createInventory(null, 27,ChatColor.translateAlternateColorCodes('&', nume));
			loadPetInventory();
			p.openInventory(petInventory);
			return;
		}
		createInventory();
		p.openInventory(mainPage);
		return;
	}
	//Inventory init
	private void createInventory() {
		cat = createItemMainPage("Cat");
		dog = createItemMainPage("Dog");
		mainPage.setItem(12, cat);
		mainPage.setItem(14, dog);
		
	}
	private void loadPetInventory() {
		barrier = createPetItem("Delete");
		rename = createPetItem("Rename");
		if(p.hasPermission("loyalpets.rename")) {
			petInventory.setItem(18, rename);
		}
		if(p.hasPermission("loyalpets.delete")) {
		petInventory.setItem(26, barrier);
		}
		skills = createPetItem("Skills");
		petInventory.setItem(16, skills);
		petInventory.setItem(10, createPetItem("Name","%petName",nume));
		petInventory.setItem(12, createPetItem("Level","%petLevel",lvl+""));
		petInventory.setItem(14, createPetItem("Xp","%petXp",xp+"","%petXpNeeded"));
		
	}
	//Utility
	private void loadValues() {
		nume = cfg.getCfg().getString("Pet.Name");
		lvl = cfg.getCfg().getInt("Pet.Level");
		xp = cfg.getCfg().getInt("Pet.Xp");
		skillPoints = cfg.getCfg().getInt("Pet.SkillPoints");
	}
	//MainPage item
	private ItemStack createItemMainPage(String path) {
		ItemStack i = new ItemStack(Material.valueOf(pl.getGui().getString("MainPage."+path+".Type")));
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("MainPage."+path+".Name")));
		im.setLore(retLore(pl.getGui().getStringList("MainPage."+path+".Lore")));
		i.setItemMeta(im);
		return i;
		
	}
	//PetInventory item
	private ItemStack createPetItem(String path,String toReplace,String replaced) {
		ItemStack i = new ItemStack(Material.valueOf(pl.getGui().getString("PetInventory."+path+".Type")));
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("PetInventory."+path+".Name")).replaceAll(toReplace, replaced));
		i.setItemMeta(im);
		return i;
		
	}
	private ItemStack createPetItem(String path,String toReplace,String replaced,String secondReplace) {
		ItemStack i = new ItemStack(Material.valueOf(pl.getGui().getString("PetInventory."+path+".Type")));
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("PetInventory."+path+".Name")).replaceFirst(toReplace, replaced).replaceFirst(secondReplace, lvl*multiplier+""));
		i.setItemMeta(im);
		return i;
		
	}
	private ItemStack createPetItem(String path) {
		ItemStack i = new ItemStack(Material.valueOf(pl.getGui().getString("PetInventory."+path+".Type")));
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', pl.getGui().getString("PetInventory."+path+".Name")));
		im.setLore(retLore(pl.getGui().getStringList("PetInventory."+path+".Lore")));
		i.setItemMeta(im);
		return i;
		
	}
	//Get lore
	private List<String> retLore(List<String>lore){
		List<String> toRet = new ArrayList<String>();
		for(String s : lore) {
			toRet.add(ChatColor.translateAlternateColorCodes('&', s).replaceAll("%petSkillPoints", skillPoints+""));
		}
		return toRet;
	}
	
	//Events
	//Cancel Main Page and pet inventory
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory().equals(mainPage)||e.getInventory().equals(petInventory)) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onClickItem(InventoryClickEvent e) {
		if(e.getClickedInventory()==null) {
			return;
		}
		if(e.getClickedInventory().equals(mainPage)) {
		if(e.getCurrentItem()==null) {
			return;
		}
		if(e.getCurrentItem().equals(dog)) {
			new PetNameChecker(p,cfg,"dog");
			p.closeInventory();
			end();
		}
		if(e.getCurrentItem().equals(cat)) {
			new PetNameChecker(p,cfg,"cat");
			p.closeInventory();
			end();
		}
		return;
		}
		if(e.getClickedInventory().equals(petInventory)) {
			if(e.getCurrentItem()==null) {
				return;
			}
			e.setCancelled(true);
		if(e.getCurrentItem().equals(barrier)) {
			pl.playerM.removePlayer(p);
			cfg.getCfg().set("Pet", null);
			cfg.save();
			p.closeInventory();
			end();
			
		}
		if(e.getCurrentItem().equals(rename)) {
			pl.playerM.getHandle(p).setRenameH(new RenamePetHandle(p));
			p.closeInventory();
			end();
		}
		if(e.getCurrentItem().equals(skills)) {
			pl.playerM.getHandle(p).setSkillinv(new SkillInventory(p,cfg));
			end();
		}
		return;
		}
	}
	@EventHandler
	public void onExit(InventoryCloseEvent e) {
		if(e.getInventory().equals(mainPage)||e.getInventory().equals(petInventory)) {
			end();
		}
	}
	public void end() {
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
