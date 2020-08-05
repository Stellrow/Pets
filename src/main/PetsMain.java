package main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import commands.PetsCommands;
import guihandler.GuiManager;
import petevents.ExperienceEvents;
import playerhandler.ConfigManager;
import playerhandler.PlayerEvents;
import playerhandler.PlayerManager;

public class PetsMain extends JavaPlugin{
	//Player manager holding player/dog
	public PlayerManager playerM;
	//Config manager holding player/config
	public ConfigManager configM;
	//Gui manager
	public GuiManager guiM;
	//GuiConfig
	private File gui;
	private FileConfiguration guicfg;
	//Folder
	public File teamsFolder;
	
	public void onEnable() {
		loadConfig();
		loadFolder();
		registerInstances();
		registerEvents();
		registerCommands();
		createGui();
	}
	public void onDisable() {
		playerM.cleanUp();
	}
	
	private void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	//Folder init
    private void loadFolder() {
		teamsFolder = new File("plugins/LoyalPets/players/");
		if(!teamsFolder.exists()) {
			teamsFolder.mkdir();
		}
	}
    //Instances init
    private void registerInstances() {
    	configM = new ConfigManager();
    	playerM = new PlayerManager();
		
		guiM = new GuiManager();
		
    }
    //Events init
    private void registerEvents() {
    	getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    	getServer().getPluginManager().registerEvents(new ExperienceEvents(), this);
    }
    //Commands init
    private void registerCommands() {
    	getCommand("pets").setExecutor(new PetsCommands());
    }
    //Reload check
    //TODO
    //Create gui.yml
    private void createGui() {
    	gui = new File(getDataFolder(),"gui.yml");
    	if(!gui.exists()) {
    		gui.getParentFile().mkdirs();
    		saveResource("gui.yml",true);
    	}
    	loadGui();
    }
    private void loadGui() {
    	guicfg = YamlConfiguration.loadConfiguration(gui);
    }
    public void saveGui() {
    	try {
			guicfg.save(gui);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public FileConfiguration getGui() {
    	return guicfg;
    }

}
