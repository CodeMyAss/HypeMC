package me.loogeh.Hype;

import java.util.ArrayList;
import java.util.Random;

import me.loogeh.Hype.SQL.MySQL;
import me.loogeh.Hype.SQL.Permissions;
import me.loogeh.Hype.Squads2.Squads;
import me.loogeh.Hype.Squads2.Squads.SquadType;
import me.loogeh.Hype.event.PlayerArmourEvent;
import me.loogeh.Hype.event.PlayerNakedEvent;
import me.loogeh.Hype.shops.Shops;
import me.loogeh.Hype.util.utilMath;
import me.loogeh.Hype.util.utilPlayer;
import me.loogeh.Hype.util.utilServer;
import me.loogeh.Hype.util.utilWorld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Listener implements org.bukkit.event.Listener {
	public static Hype plugin;

	public Listener(Hype instance) {
		plugin = instance;
	}
	private final String possibleColours = "0123456789abcdefklmnor";
	private final String colourChar = "&";
	public static ArrayList<String> lightning = new ArrayList<String>();
	//	private int armour[] = {0, 1, 2, 3, 4, 5, 6, 7};
	//	private int weapons[] = {16, 17, 18, 19, 20, 21};
	//	private int tools[] = {32, 33, 34, 35};
	//	private int bows[] = {48, 49, 50, 51};
	Random rand = new Random();


	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Handlers.handleBlockPlaceEvent(event);
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if(event.getEntity() instanceof Creeper) event.blockList().clear();
		else if(event.getEntity() instanceof Fireball) event.blockList().clear();
		else if(event.getEntity() instanceof SmallFireball) event.blockList().clear();
		else if(event.getEntity() instanceof EnderDragon) event.blockList().clear();
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String sqlCmd = event.getMessage().replaceAll("'", "");
		MySQL.doUpdate("INSERT INTO `chat_log`(`player`, `message`) VALUES ('" + event.getPlayer().getName() + "','" + sqlCmd.toString() + "')");
		String cmd = event.getMessage();
		if(cmd.charAt(0) == '/') cmd = cmd.replaceFirst("/", "");
		cmd = cmd.split(" ")[0];
		if(!UnknownCommand.commandExists(cmd)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "Server - " + ChatColor.WHITE + "Command doesn't exist /help for help");
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Handlers.handleRespawn(event);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Handlers.handleBlockBreak(event);
	}


	@EventHandler
	public void onPlayerKilled(PlayerDeathEvent event) {
		Handlers.handlePlayerDeathEvent(event);
//		final Player player = event.getEntity();
//		Player killer = player.getKiller();
//		PlayerDeathEvent deathMessage = (PlayerDeathEvent) event;
//		event.setDroppedExp(0);
//		if(Arena.isPlaying(player)) {
//			return;
//		}
//		if(killer == null) {
//			event.setDeathMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " died");
//			return;
//		}
//		if(player instanceof Player) {
//			if(killer instanceof Player) {
//				if(killer != null) {
//					ItemStack item = killer.getItemInHand();
//					String itemH = item.getType().toString().replaceAll("_", " ").toLowerCase();
//					String itemF = WordUtils.capitalize(itemH);
//					int e = utilItems.countLevels(item);
//					if(e > 0) {
//						deathMessage.setDeathMessage(ChatColor.YELLOW + killer.getName() + ChatColor.WHITE + " killed " + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " with " + ChatColor.GREEN + "L" + e + " " + itemF);
//					}
//					deathMessage.setDeathMessage(ChatColor.YELLOW + killer.getName() + ChatColor.WHITE + " killed " + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " with " + ChatColor.GREEN + itemF);
//					if(!Arena.isPlaying(player) && !Arena.isPlaying(killer) && !Spleef.isSpleefing(player) && !Spleef.isSpleefing(killer) && !ArcticBrawl.isArcting(player) && !ArcticBrawl.isArcting(killer)) {
//						int pMoney = Money.getMoney(player);
//						int percMoney = Math.round((int) (pMoney * 0.05));
//						int enchMoney = Math.round((int) (pMoney * 0.11));
//						if(utilPlayer.hasArmour(player)) {
//							if(pMoney < 200000) {
//								if(!player.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) {
//									Money.addMoney(killer, percMoney);
//									Money.subtractMoney(player, percMoney);
//									killer.sendMessage(ChatColor.GRAY + "You earned " + ChatColor.DARK_GREEN + "$" + percMoney + ChatColor.GRAY + " for killing " + ChatColor.GREEN + player.getName());
//									player.sendMessage(ChatColor.GRAY + killer.getName() + " stole" + ChatColor.DARK_GREEN + " $" + percMoney + ChatColor.GRAY + " from you");
//								} else {
//									Money.addMoney(killer, enchMoney);
//									Money.subtractMoney(player, enchMoney);
//									killer.sendMessage(ChatColor.GRAY + "You earned " + ChatColor.DARK_GREEN + "$" + enchMoney + ChatColor.GRAY + " for killing " + ChatColor.GREEN + player.getName());
//									player.sendMessage(ChatColor.GRAY + killer.getName() + " stole" + ChatColor.DARK_GREEN + " $" + enchMoney + ChatColor.GRAY + " from you");
//								}
//							} else if(pMoney >= 200000) {
//								if(!player.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) {
//									Money.addMoney(killer, 10000);
//									Money.subtractMoney(player, 10000);
//									killer.sendMessage(ChatColor.GRAY + "You earned " + ChatColor.DARK_GREEN + "$10000" + ChatColor.GRAY + " for killing " + ChatColor.GREEN + player.getName());
//									player.sendMessage(ChatColor.GRAY + killer.getName() + " stole" + ChatColor.DARK_GREEN + " $10000" + ChatColor.GRAY + " from you");
//								} else {
//									Money.addMoney(killer, 22000);
//									Money.subtractMoney(player, 22000);
//									killer.sendMessage(ChatColor.GRAY + "You earned " + ChatColor.DARK_GREEN + "$22000" + ChatColor.GRAY + " for killing " + ChatColor.GREEN + player.getName());
//									player.sendMessage(ChatColor.GRAY + killer.getName() + " stole" + ChatColor.DARK_GREEN + " $22000" + ChatColor.GRAY + " from you");
//								}
//							}
//						} else {
//							killer.sendMessage(ChatColor.GRAY + "You earned no money because your victim had no " + ChatColor.YELLOW + "Armour");
//						}
//					}
//				}
//			}
//		} else if(!(killer instanceof Player)) {
//			deathMessage.setDeathMessage(ChatColor.GRAY + "Death - " + ChatColor.YELLOW + player.getName());
//		}

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Handlers.handleJoinEvent(event);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Handlers.handleLeaveEvent(event);
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Handlers.handleLoginEvent(event);
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Handlers.handleEntityDeath(event);
	}
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		Handlers.handleBlockSpread(event);
	}
	
	@EventHandler
	public void onItemChange(PlayerItemHeldEvent event) {
		Handlers.handleItemChange(event);
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Handlers.handleTeleport(event);
	}
	
	@EventHandler
	public void onProjectileShoot(ProjectileLaunchEvent event) {
		Handlers.handleArrowShoot(event);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Handlers.handleEntityDamage(event);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Handlers.handleEntityDamageByEntity(event);
	}
	
	@EventHandler
	public void fish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		//		int tools_ench = utilMath.getRandom(0, 8);
		//		int tools_amt = utilMath.getRandom(1, 4);
		//		int sword_ench = utilMath.getRandom(16, 21);
		//		int sword_amt = utilMath.getRandom(1, 4);
		//		int bow_ench = utilMath.getRandom(48, 51);
		//		int bow_amt = utilMath.getRandom(1, 4);
		//		int armour_ench = utilMath.getRandom(0, 7);
		//		int armour_amt = utilMath.getRandom(1, 4);
		if(event.getState() == State.IN_GROUND) {
			if(utilPlayer.isInLiquid(player)) {
				return;
			}
			if(player.getLocation().getPitch() > -35) {
				return;
			}
			if(Squads.isClaimed(utilWorld.chunkToStr(player.getLocation().getChunk()))) {
				String owner = Squads.getOwnerChunk(player.getLocation().getChunk());
				if(Squads.hasSquad(player.getName())) {
					if(!owner.equalsIgnoreCase(Squads.getSquad(player.getName())) && (!(Squads.getType(owner) == SquadType.ADMIN))) {
						player.sendMessage(ChatColor.BLUE + "Squads - " + ChatColor.WHITE + "You cannot use " + ChatColor.YELLOW + "Grappel Hook " + ChatColor.WHITE + "in " + ChatColor.YELLOW + Squads.getOwnerChunk(player.getLocation().getChunk()));
						return;
					}
				}
			}
			if(event.getHook().getLocation().getY() < player.getLocation().getY() + 3) {
				return;
			}
			double compar = event.getHook().getLocation().getY() - player.getLocation().getY();
			if(compar < 6) {
				player.setVelocity(player.getLocation().getDirection().multiply(event.getHook().getLocation().getY() * 1.34 / player.getLocation().getY()));
			} else if(compar < 12 && compar > 6) {
				player.setVelocity(player.getLocation().getDirection().multiply(event.getHook().getLocation().getY() * 1.6 / player.getLocation().getY()));
			} else if(compar < 18 && compar > 12) {
				player.setVelocity(player.getLocation().getDirection().multiply(event.getHook().getLocation().getY() * 1.9 / player.getLocation().getY()));
			} else if(compar < 24 && compar > 18) {
				player.setVelocity(player.getLocation().getDirection().multiply(event.getHook().getLocation().getY() * 2 / player.getLocation().getY()));
			} else if(compar < 30 && compar > 24) {
				player.setVelocity(player.getLocation().getDirection().multiply(event.getHook().getLocation().getY() * 2.1 / player.getLocation().getY()));
			} else {
				player.setVelocity(player.getLocation().getDirection().multiply(event.getHook().getLocation().getY() * 2.2 / player.getLocation().getY()));
			}
		}
		int fish_amt = utilMath.getRandom(1, 28);
		
		if(event.getState() == State.CAUGHT_FISH) {
			player.getInventory().addItem(new ItemStack(Material.RAW_FISH, fish_amt));
			player.sendMessage(ChatColor.GRAY + "You caught " + ChatColor.GREEN + (fish_amt + 1) + ChatColor.GRAY + " Fish");
			//			if(utilMath.getRandom(1, 75) == 1) {
			//					ItemStack item = (ItemStack) new ItemStack(Material.DIAMOND_SPADE);
			//					item.addEnchantment(Enchantment.getById(tools[tools_ench]), tools_amt);
			//					player.getInventory().addItem(item);
			//					player.sendMessage(ChatColor.AQUA + "You have been granted an " + ChatColor.GREEN + "Enchanted Shovel!");
			//				}
			//			else if(utilMath.getRandom(76, 226) == 83) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE));
			//				item.getItemStack().addEnchantment(Enchantment.getById(tools[tools_ench]), tools_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted an " + ChatColor.GREEN + "Enchanted Pickaxe!");
			//
			//				}
			//			else if(utilMath.getRandom(227, 302) == 250) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.DIAMOND_AXE));
			//				item.getItemStack().addEnchantment(Enchantment.getById(tools[tools_ench]), tools_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted an " + ChatColor.GREEN + "Enchanted Axe!");
			//
			//				}
			//			else if(utilMath.getRandom(303, 553) == 333) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
			//				item.getItemStack().addEnchantment(Enchantment.getById(weapons[sword_ench]), sword_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted an " + ChatColor.GREEN + "Enchanted Sword!");
			//
			//				}
			//			else if(utilMath.getRandom(554, 604) == 602) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.BOW));
			//				item.getItemStack().addEnchantment(Enchantment.getById(bows[bow_ench]), bow_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted an " + ChatColor.GREEN + "Enchanted Pickaxe!");
			//
			//				}
			//			else if(utilMath.getRandom(605, 1004) == 803) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_HELMET));
			//				Item item2 = (Item) player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			//				Item item3 = (Item) player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_LEGGINGS));
			//				Item item4 = (Item) player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_BOOTS));
			//				item.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item2.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item3.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item4.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted " + ChatColor.GREEN + "Enchanted Chain Armour!");
			//
			//				}
			//			else if(utilMath.getRandom(1005, 1405) == 803) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.GOLD_HELMET));
			//				Item item2 = (Item) player.getInventory().addItem(new ItemStack(Material.GOLD_CHESTPLATE));
			//				Item item3 = (Item) player.getInventory().addItem(new ItemStack(Material.GOLD_LEGGINGS));
			//				Item item4 = (Item) player.getInventory().addItem(new ItemStack(Material.GOLD_BOOTS));
			//				item.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item2.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item3.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item4.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted " + ChatColor.GREEN + "Enchanted Gold Armour!");
			//
			//				}
			//			else if(utilMath.getRandom(1406, 1806) == 803) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.IRON_HELMET));
			//				Item item2 = (Item) player.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
			//				Item item3 = (Item) player.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS));
			//				Item item4 = (Item) player.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
			//				item.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item2.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item3.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item4.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted " + ChatColor.GREEN + "Enchanted Iron Armour!");
			//
			//				}
			//			else if(utilMath.getRandom(1807, 2207) == 803) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.DIAMOND_HELMET));
			//				Item item2 = (Item) player.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
			//				Item item3 = (Item) player.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
			//				Item item4 = (Item) player.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS));
			//				item.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item2.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item3.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item4.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted " + ChatColor.GREEN + "Enchanted Diamond Armour!");
			//
			//				}
			//			else if(utilMath.getRandom(2208, 2608) == 803) {
			//				Item item = (Item) player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
			//				Item item2 = (Item) player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
			//				Item item3 = (Item) player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
			//				Item item4 = (Item) player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
			//				item.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item2.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item3.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				item4.getItemStack().addEnchantment(Enchantment.getById(armour[armour_ench]), armour_amt);
			//				player.sendMessage(ChatColor.AQUA + "You have been granted " + ChatColor.GREEN + "Enchanted Leather Armour!");
			//
			//			
			//			}
		}
	}

	@EventHandler
	public void ping(ServerListPingEvent event) {
		event.setMotd(ChatColor.DARK_AQUA + "HypeMC " + ChatColor.WHITE + " - " + ChatColor.AQUA + " Australia");
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Handlers.handleInventoryClick(event);
	}
	
	@EventHandler
	public void onPlayerNaked(PlayerNakedEvent event) {
		Player player = event.getPlayer();
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
	}
	
	@EventHandler
	public void onPlayerArmour(PlayerArmourEvent event) {
		Player player = event.getPlayer();
		if(player.getActivePotionEffects().contains(PotionEffectType.SPEED)) player.removePotionEffect(PotionEffectType.SPEED);
	}

	@EventHandler
	public void onFoodEat(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Material hand = player.getItemInHand().getType();
		Action action = event.getAction();
		if(hand == Material.MUSHROOM_SOUP) {
			if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				if(player.getHealth() == player.getMaxHealth()) return;
				else if(player.getMaxHealth() - player.getHealth() < 4.0D) player.setHealth(20.0D);
				else player.setHealth(player.getHealth() + 4.0D);
				event.setUseItemInHand(Result.DENY);
				player.setItemInHand(null);
			}
		}
	}

//	@EventHandler
//	public void lightningSceptre(PlayerInteractEvent event) {
//		Player player = event.getPlayer();
//		if(player.getItemInHand().getType() == Material.DIAMOND_HOE) {
//			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
//				if(Cooldown.isCooling(player.getName(), "lightning")) {
//					Cooldown.coolDurMessage(player, "lightning");
//					return;
//				}
//				Cooldown.add(player.getName(), "lighting", 12, System.currentTimeMillis());
//				player.getWorld().strikeLightning(player.getTargetBlock(null, 50).getLocation());
//				utilPlayer.abilityMessage(player, "Lightning Sceptre");
//			}
//		}
//	}

	@EventHandler
	public void SignPlace(SignChangeEvent event) {
		for (int forInt = 0; forInt < 4; forInt++) {
			if (event.getLine(forInt).isEmpty())
				continue;
			String line = event.getLine(forInt);
			String[] splitLine = line.split(colourChar);
			String lineNew = "";
			if (splitLine.length == 0) {
				lineNew += colourChar;
			} else {
				lineNew += splitLine[0];
				for (int i = 1; i < splitLine.length; i++) {
					try {
						char cha = splitLine[i].toLowerCase().charAt(0);
						String string = Character.toString(cha);
						if (possibleColours.contains(string)) {
							lineNew += "\u00A7";
						} else {
							lineNew += colourChar;
						}
						lineNew += splitLine[i];
					} catch (StringIndexOutOfBoundsException e) {
						lineNew += colourChar;
					}
				}
			}
			event.setLine(forInt, lineNew);
		}
	}
	
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		Handlers.handlePlayerPortal(event);
	}

	@EventHandler
	public void entDeath(EntityDeathEvent event) {
		Entity ent = event.getEntity();
		event.setDroppedExp(0);
		if(ent.getType() == EntityType.CREEPER) {
			event.getDrops().clear();
			Item item = ent.getWorld().dropItemNaturally(ent.getLocation().add(0, 1, 0), new ItemStack(Material.COAL, utilMath.getRandom(0, 3)));
			item.setPickupDelay(0);
		} else if(ent.getType() == EntityType.SHEEP) {
			event.getDrops().clear();
			Item beef = ent.getWorld().dropItemNaturally(ent.getLocation().add(0, 1, 0), new ItemStack(Material.RAW_BEEF, utilMath.getRandom(0, 2)));
			Item wool = ent.getWorld().dropItemNaturally(ent.getLocation().add(0, 1, 0), new ItemStack(Material.WOOL, utilMath.getRandom(0, 2)));
			beef.setPickupDelay(0);
			wool.setPickupDelay(0);
		} else if(ent.getType() == EntityType.ZOMBIE) {
			event.getDrops().clear();
			Item flesh = ent.getWorld().dropItemNaturally(ent.getLocation().add(0, 1, 0), new ItemStack(Material.ROTTEN_FLESH, utilMath.getRandom(0, 4)));
			flesh.setPickupDelay(0);
		} else if(ent.getType() == EntityType.SKELETON) {
			event.getDrops().clear();
			Item bone = ent.getWorld().dropItemNaturally(ent.getLocation().add(0, 1, 0), new ItemStack(Material.BONE, utilMath.getRandom(0, 4)));
			Item arrow = ent.getWorld().dropItemNaturally(ent.getLocation().add(0, 1, 0), new ItemStack(Material.ARROW, utilMath.getRandom(0, 3)));
			arrow.setPickupDelay(0);
			bone.setPickupDelay(0);
		}
	}

	@EventHandler
	public void bucket(PlayerBucketFillEvent event) {
		Material fluid = event.getBlockClicked().getType();
		if(fluid == Material.LAVA || fluid == Material.STATIONARY_LAVA) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Server - " + ChatColor.GRAY + "You have to buy " + ChatColor.YELLOW + "Lava buckets");
		}
		
	}

	@EventHandler
	public void bucketEmpty(PlayerBucketEmptyEvent event) {
		if(utilServer.isInSpawn(event.getPlayer())) {
			if(Permissions.isAdmin(event.getPlayer())) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void portalCreate(PortalCreateEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void enchItem(EnchantItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void creatureSpawn(CreatureSpawnEvent event) {
		Handlers.handleCreatureSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPotionSplash(PotionSplashEvent event) {
		if(Shops.isInShops(event.getPotion().getLocation())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockFadeEvent(BlockFadeEvent event) {
		Handlers.handleBlockDecay(event);
	}
	
	@EventHandler
	public void onBlockFromToEvent(BlockFromToEvent event) {
		Handlers.handleBlockTo(event);
	}
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		Handlers.handleLeavesDecay(event);
	}
	

}
