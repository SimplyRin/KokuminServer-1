package me.nucha.kokumin.listeners.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.nucha.kokumin.Main;
import me.nucha.kokumin.utils.CustomItem;

public class GuiPotionMaker implements Listener {

	public static Main plugin;

	public static HashMap<Player, PotionEffectType> potiontypes;
	public static HashMap<Player, PotionEffect> potioneffect;
	public static HashMap<Player, List<PotionEffect>> potioneffects;
	public static List<Player> sayduration;
	public static List<Player> sayamplifier;
	public static List<Player> splash;

	public GuiPotionMaker() {
		plugin = Main.getInstance();

		potiontypes = new HashMap<>();
		potioneffect = new HashMap<>();
		potioneffects = new HashMap<>();
		sayduration = new ArrayList<Player>();
		sayamplifier = new ArrayList<Player>();
		splash = new ArrayList<Player>();
	}

	public static void open(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9 * 4, "§aPotion Maker");
		boolean first = true;
		for (PotionEffectType pottype : PotionEffectType.values()) {
			if (first) {
				first = false;
				continue;
			}
			ItemStack itempotion = new CustomItem(Material.POTION, 1, pottype.getName(), "クリックして適用");
			PotionMeta pmeta = ((PotionMeta) itempotion.getItemMeta());
			pmeta.addCustomEffect(pottype.createEffect(60, 0), true);
			itempotion.setItemMeta(pmeta);
			inv.addItem(itempotion);
		}
		if (splash.contains(p)) {
			inv.setItem(27, new CustomItem(Material.SULPHUR, 1, "§eスプラッシュタイプ"));
		} else {
			inv.setItem(27, new CustomItem(Material.SUGAR, 1, "§aドリンクタイプ"));
		}
		inv.setItem(26, new CustomItem(Material.GLASS_BOTTLE, 1, "§cリセット"));
		inv.setItem(35, new CustomItem(Material.REDSTONE_BLOCK, 1, "§c終了"));
		p.openInventory(inv);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		if (p.getOpenInventory().getTopInventory() != null) {
			if (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§aPotion Maker")) {
				event.setCancelled(true);
				if (event.getSlotType().equals(SlotType.OUTSIDE) || event.getCurrentItem().getType().equals(Material.AIR)) {
					return;
				}
				if (event.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
					ItemStack item = event.getCurrentItem();
					if (item.getType().equals(Material.POTION)) {
						List<PotionEffect> potionEffects = ((PotionMeta) item.getItemMeta()).getCustomEffects();
						potiontypes.put(p, potionEffects.get(0).getType());
						sayduration.add(p);
						p.sendMessage("§e効果の長さをチャットで発言してください(秒)");
						p.closeInventory();
					}
					if (item.getType().equals(Material.GLASS_BOTTLE)) {
						potioneffect.remove(p);
						potioneffects.remove(p);
						potiontypes.remove(p);
						sayamplifier.remove(p);
						sayduration.remove(p);
						p.sendMessage("§a設定をリセットしました");
					}
					if (item.getType().equals(Material.REDSTONE_BLOCK)) {
						p.closeInventory();
						if (!potioneffects.containsKey(p)) {
							p.sendMessage("§7何もせずに終わりました");
						} else {
							PotionEffectType type = potioneffects.get(p).get(0).getType();
							ItemStack potion = new ItemStack(Material.POTION, 1, getDurabilityOfPotion(type));
							PotionMeta meta = ((PotionMeta) potion.getItemMeta());
							for (PotionEffect pef : potioneffects.get(p)) {
								meta.addCustomEffect(pef, true);
							}

							Potion pot;
							try {
								pot = Potion.fromItemStack(potion);
							} catch (IllegalArgumentException e) {
								potioneffect.remove(p);
								potioneffects.remove(p);
								potiontypes.remove(p);
								sayamplifier.remove(p);
								sayduration.remove(p);
								p.sendMessage("§cエラーが出ました");
								return;
							}
							if (splash.contains(p)) {
								pot.setSplash(true);
							}
							pot.apply(potion);
							potion.setItemMeta(meta);
							p.getInventory().addItem(potion);
							p.sendMessage("§aポーションを作成して終わりました");
							potioneffect.remove(p);
							potioneffects.remove(p);
							potiontypes.remove(p);
							splash.remove(p);
						}
						p.updateInventory();
					}
					if (item.getType().equals(Material.SUGAR)) {
						if (!splash.contains(p)) {
							splash.add(p);
						}
						p.getOpenInventory().setItem(27, new CustomItem(Material.SULPHUR, 1, "§eスプラッシュタイプ"));
						p.playSound(p.getLocation(), Sound.CLICK, 1, 1);
					}
					if (item.getType().equals(Material.SULPHUR)) {
						if (splash.contains(p)) {
							splash.remove(p);
						}
						p.getOpenInventory().setItem(27, new CustomItem(Material.SUGAR, 1, "§aドリンクタイプ"));
						p.playSound(p.getLocation(), Sound.CLICK, 1, 1);
					}
				}
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if (sayduration.contains(p)) {
			String message = event.getMessage();
			if (StringUtils.isNumeric(message)) {
				int duration = Integer.valueOf(message);
				event.setCancelled(true);
				if (duration > 0) {
					potioneffect.put(p, new PotionEffect(potiontypes.get(p), duration * 20, 0));
					sayduration.remove(p);
					sayamplifier.add(p);
					p.sendMessage("§a次に、効果の強さをチャットで発言してください");
					return;
				} else {
					p.sendMessage("§c1以上を指定してください");
				}
			} else {
				p.sendMessage("§c数字(秒)を指定してください");
			}
		}
		if (sayamplifier.contains(p)) {
			String message = event.getMessage();
			if (StringUtils.isNumeric(message)) {
				int amplifier = Integer.valueOf(message);
				event.setCancelled(true);
				if (amplifier >= 0) {
					PotionEffect pef = potioneffect.get(p);
					List<PotionEffect> pefs = new ArrayList<>();
					if (potioneffects.containsKey(p)) {
						pefs.addAll(potioneffects.get(p));
					}
					pefs.add(new PotionEffect(pef.getType(), pef.getDuration(), amplifier));
					potioneffects.put(p, pefs);
					sayamplifier.remove(p);
					open(p);
				} else {
					p.sendMessage("§c0以上を指定してください");
				}
			} else {
				p.sendMessage("§c数字(秒)を指定してください");
			}
		}
	}

	public short getDurabilityOfPotion(PotionEffectType type) {
		short data = 8197;
		if (type.getName().equalsIgnoreCase(PotionEffectType.REGENERATION.getName()))
			data = 8193;
		if (type.getName().equalsIgnoreCase(PotionEffectType.SPEED.getName()))
			data = 8194;
		if (type.getName().equalsIgnoreCase(PotionEffectType.FIRE_RESISTANCE.getName()))
			data = 8195;
		if (type.getName().equalsIgnoreCase(PotionEffectType.HEAL.getName()))
			data = 8197;
		if (type.getName().equalsIgnoreCase(PotionEffectType.NIGHT_VISION.getName()))
			data = 8198;
		if (type.getName().equalsIgnoreCase(PotionEffectType.INCREASE_DAMAGE.getName()))
			data = 8201;
		if (type.getName().equalsIgnoreCase(PotionEffectType.JUMP.getName()))
			data = 8203;
		if (type.getName().equalsIgnoreCase(PotionEffectType.WATER_BREATHING.getName()))
			data = 8205;
		if (type.getName().equalsIgnoreCase(PotionEffectType.INVISIBILITY.getName()))
			data = 8206;
		return data;
	}
}