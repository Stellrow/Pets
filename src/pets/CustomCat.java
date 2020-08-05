package pets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import configmanagement.CustomConfig;
import guihandler.GuiHandle;
import main.PetsMain;

public class CustomCat implements Listener{
	private PetsMain pl = JavaPlugin.getPlugin(PetsMain.class);
	private LivingEntity entity;
	private Cat cat;
	private String nume;
	private Integer lvl;
	private Integer xp;
	private Integer skillPoints;
	private Player p;
	private Integer xpNeeded;
	private CustomConfig cc;
	private boolean isAutoSmelting = false;

	public CustomCat(World world,Player p,Location loc,String nume,int lvl,int xp,int skillPoints) {
		entity = (Cat)world.spawnEntity(loc, EntityType.CAT);
		cat = (Cat) entity;
		if(cat instanceof Tameable) {
			Tameable tame = (Tameable) cat;
			tame.setOwner(p);
		}
		//Load variables
		this.setSkillPoints(skillPoints);
		this.p=p;
		this.setNume(nume);
		this.setLvl(lvl);
		this.setXp(xp);
		cc=pl.configM.getConfig(p);
		xpNeeded = lvl*pl.getConfig().getInt("LevelUp.levelMultiply");
		//Assign entity
		//Invulnerability+Creative Blocker
		cat.setInvulnerable(true);
		//Name
		cat.setCustomName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("PetConfig.Name")).replaceAll("%petName", nume).replaceAll("%level",lvl+""));
		cat.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
		cat.setHealth(500);
		
		//Add entity to world and teleport
		//Ownership
				
				
		cat.teleport(loc);
		runChecks();
		checkSurroundings();
		startSkills();
		pl.getServer().getPluginManager().registerEvents(this, pl);
				
		
		
	}

	//Utility
	public void kill() {
		cat.remove();
	}
	public LivingEntity getCat() {
		return cat;
	}

	public String getNume() {
		return nume;
	}

	public void rename(String newName) {
		this.nume=newName;
		cat.setCustomName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("PetConfig.Name")).replaceAll("%petName", nume).replaceAll("%level",lvl+""));
		cc.getCfg().set("Pet.Name", nume);
		cc.save();
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public void setLvl(Integer lvl) {
		this.lvl=lvl;
	}

	public Integer getLvl() {
		return lvl;
	}
	public void addLvl(Integer lvlToAdd) {
		this.lvl=this.lvl+lvlToAdd;
		cat.setCustomName(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("PetConfig.Name")).replaceAll("%petName", nume).replaceAll("%level",lvl+""));
		cc.getCfg().set("Pet.Level", lvl);
		cc.save();
	}
	public Integer getXp() {
		return xp;
	}
	public void setXp(Integer xp) {
		this.xp = xp;
	}
	public void addXp(Integer xp) {
		this.xp+=xp;
		checkLevel();
		cc.getCfg().set("Pet.Xp", this.xp);
		cc.save();
	}
	public void decreaseXp(Integer xp) {
		this.xp-=xp;
		cc.getCfg().set("Pet.Xp", this.xp);
		cc.save();
	}
	private void checkLevel() {
		if(xp>=xpNeeded) {
			addLvl(1);
			decreaseXp(xpNeeded);
			addSkillPoints(1);
			xpNeeded = lvl*pl.getConfig().getInt("LevelUp.levelMultiply");
		}
	}
	//Events
		@EventHandler
		public void onRClick(PlayerInteractEntityEvent e) {
			if(e.getRightClicked().equals(cat)) {
				e.setCancelled(true);
				if(e.getPlayer().equals(p)) {
				pl.playerM.managePlayer(p);
				pl.playerM.getHandle(p).setHandler(new GuiHandle(p,cc));;
				}
			}
		}

		public Integer getSkillPoints() {
			return skillPoints;
		}

		public void addSkillPoints(Integer toAdd) {
			skillPoints+=toAdd;
			cc.getCfg().set("Pet.SkillPoints", skillPoints);
			cc.save();
		}
		public void setSkillPoints(Integer skillPoints) {
			this.skillPoints = skillPoints;
		}
		public void decreaseSkillPoints(Integer toDec) {
			this.skillPoints-=toDec;
			cc.getCfg().set("Pet.SkillPoints", skillPoints);
			cc.save();
		}
		
		
		//Stay close
		@EventHandler
		public void onTeleport(PlayerTeleportEvent e) {
			if(e.getPlayer().equals(p)) {
				if(e.getTo().getWorld().equals(e.getFrom().getWorld())) {
					cat.teleport(p);
				}
				
			}
		}
		@EventHandler
		public void onChangeWorld(PlayerChangedWorldEvent e) {
			if(e.getPlayer().equals(p)) {
				cat.teleport(p);
			}
		}
		
		public void runChecks() {
			new BukkitRunnable() {

				@Override
				public void run() {
					if(cat.getWorld().equals(p.getWorld())) {
						cat.getLocation().getChunk().load();
					if(cat.getLocation().distance(p.getLocation())>=30) {
						cat.teleport(p);
					}
					
				}
				}
			}.runTaskTimer(pl, 0, 60);
		}
		
		public void checkSurroundings() {
			new BukkitRunnable() {

				@Override
				public void run() {
					for(Entity e : cat.getWorld().getNearbyEntities(cat.getLocation(), 30, 30, 30)) {
						if(e instanceof Cat) {
							Cat wo = (Cat)e;
							if(wo.getCustomName()==null) {
								return;
							}
							if(wo.getCustomName().equalsIgnoreCase(cat.getCustomName())) {
								if(!wo.equals(cat)) {
									wo.remove();
								}
								
							}
						}
					}
					
				}
				
			}.runTaskTimer(pl, 0, 10*20);
		}
		
		//Skills
		public void startSkills() {
			new BukkitRunnable() {

				@Override
				public void run() {
					loadAndPickSkill();
					
				}
				
			}.runTaskTimer(pl, 0, pl.getConfig().getInt("SkillsConfig.timeBetweenSkills")*20);
		}
		public void loadAndPickSkill() {
			List<String> activeSkills = new ArrayList<String>();
			if(cc.getCfg().contains("Pet.Skills")) {
			for(String s : cc.getCfg().getConfigurationSection("Pet.Skills").getKeys(false)) {
				if(cc.getCfg().getBoolean("Pet.Skills."+s)) {
					activeSkills.add(s);
				}
			}
			if(activeSkills.isEmpty()) {
				return;
			}
			
			Random rnd = new Random();
			doSkill(activeSkills.get(rnd.nextInt(activeSkills.size())));
			
			}
		}
		public void doSkill(String skill) {
			getCat().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, getCat().getLocation().add(0.5, 0.5, 0.5),1);
			getCat().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, getCat().getLocation().add(0.25,0.25,0.25),1);
			getCat().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, getCat().getLocation(), 1);
			p.playSound(p.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1, 1);
			//Mechanic
			if(skill.equalsIgnoreCase("speed")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,pl.getConfig().getInt("SkillsConfig.speedEffect")*20,0));
				return;
			}
			if(skill.equalsIgnoreCase("haste")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,pl.getConfig().getInt("SkillsConfig.hasteEffect")*20,0));
				return;
			}
			if(skill.equalsIgnoreCase("regen")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,pl.getConfig().getInt("SkillsConfig.regenEffect")*20,0));
				return;
			}
			if(skill.equalsIgnoreCase("strength")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,pl.getConfig().getInt("SkillsConfig.strengthEffect")*20,0));
				return;
			}
			if(skill.equalsIgnoreCase("nightvision")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,pl.getConfig().getInt("SkillsConfig.nightvisionEffect")*20,0));
				return;
			}
			if(skill.equalsIgnoreCase("nutrition")) {
				p.setFoodLevel(20);
				return;
			}
			if(skill.equalsIgnoreCase("autosmelt")) {
				isAutoSmelting=true;
				new BukkitRunnable() {

					@Override
					public void run() {
						isAutoSmelting=false;
						
					}
					
				}.runTaskLater(pl, pl.getConfig().getInt("SkillsConfig.autosmeltEffect")*20);
			}
		}
		@EventHandler
		public void onBreak(BlockBreakEvent e) {
			if(e.getPlayer().equals(p)) {
				if(isAutoSmelting) {
					if(e.getBlock().getType()==Material.IRON_ORE) {
						e.setDropItems(false);
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT,2));
						return;
					}
					if(e.getBlock().getType()==Material.GOLD_ORE) {
						e.setDropItems(false);
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT,2));
						return;
					}
					if(e.getBlock().getType()==Material.SAND) {
						e.setDropItems(false);
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GLASS,2));
						return;
					}
					
				}
			}
		}
		
		
		//End Skills

}
