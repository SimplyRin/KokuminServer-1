package me.nucha.kokumin.kabu;

import java.util.ArrayList;
import java.util.List;

import me.nucha.kokumin.coin.Coin;
import me.nucha.kokumin.utils.CustomItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiKabu implements Listener {

	private static KabuManager kabuManager;
	public static List<Player> viewers;

	public GuiKabu(KabuManager kabuManager) {
		kabuManager(kabuManager);
	}

	public static void kabuManager(KabuManager kabuManager) {
		GuiKabu.kabuManager = kabuManager;
		viewers = new ArrayList<Player>();
	}

	public static void open(Player p) {
		Inventory gui = Bukkit.createInventory(null, 5 * 9,
				"§6§l株1個の値段: §e§l" + kabuManager.getPrice() + "円");
		ItemStack buy1 = new CustomItem(Material.STAINED_CLAY, 1,
				"§2株を購入する", 5,
				"§a数: " + 1,
				"§a値段: " + 1 * kabuManager.getPrice() + "円");
		ItemStack buy5 = new CustomItem(Material.STAINED_CLAY, 5,
				"§2株を購入する", 5,
				"§a数: " + 5,
				"§a値段: " + 5 * kabuManager.getPrice() + "円");
		ItemStack buy10 = new CustomItem(Material.STAINED_CLAY, 10,
				"§2株を購入する", 5,
				"§a数: " + 10,
				"§a値段: " + 10 * kabuManager.getPrice() + "円");
		ItemStack buy25 = new CustomItem(Material.STAINED_CLAY, 25,
				"§2株を購入する", 5,
				"§a数: " + 25,
				"§a値段: " + 25 * kabuManager.getPrice() + "円");
		ItemStack buy50 = new CustomItem(Material.STAINED_CLAY, 50,
				"§2株を購入する", 5,
				"§a数: " + 50,
				"§a値段: " + 50 * kabuManager.getPrice() + "円");
		ItemStack sell1 = new CustomItem(Material.STAINED_CLAY, 1,
				"§3株を売却する", 3,
				"§b数: " + 1,
				"§b値段: " + 1 * kabuManager.getPrice() + "円");
		ItemStack sell5 = new CustomItem(Material.STAINED_CLAY, 5,
				"§3株を売却する", 3,
				"§b数: " + 5,
				"§b値段: " + 5 * kabuManager.getPrice() + "円");
		ItemStack sell10 = new CustomItem(Material.STAINED_CLAY, 10,
				"§3株を売却する", 3,
				"§b数: " + 10,
				"§b値段: " + 10 * kabuManager.getPrice() + "円");
		ItemStack sell25 = new CustomItem(Material.STAINED_CLAY, 25,
				"§3株を売却する", 3,
				"§b数: " + 25,
				"§b値段: " + 25 * kabuManager.getPrice() + "円");
		ItemStack sell50 = new CustomItem(Material.STAINED_CLAY, 50,
				"§3株を売却する", 3,
				"§b数: " + 50,
				"§b値段: " + 50 * kabuManager.getPrice() + "円");
		gui.setItem(11, buy1);
		gui.setItem(12, buy5);
		gui.setItem(13, buy10);
		gui.setItem(14, buy25);
		gui.setItem(15, buy50);
		gui.setItem(29, sell1);
		gui.setItem(30, sell5);
		gui.setItem(31, sell10);
		gui.setItem(32, sell25);
		gui.setItem(33, sell50);
		ItemStack panel = new CustomItem(Material.STAINED_GLASS_PANE, 1, " ", 15);
		int[] panelSlots = new int[] { 0, 1, 7, 8,
				9, 10, 16, 17,
				18, 19, 25, 26,
				27, 28, 34, 35,
				36, 37, 43, 44 };
		for (int slot : panelSlots) {
			gui.setItem(slot, panel);
		}
		CustomItem changes = new CustomItem(Material.PAPER, 1, "§e株価の変動");
		if (kabuManager.getPriceChanges() != null) {
			changes.setLore(kabuManager.getPriceChanges());
		}
		ItemStack tutorial = new CustomItem(Material.BOOK, 1, "§a§l安く買い", "§3§l高く売って", "§e§l利益を得よう");
		ItemStack kabus = new CustomItem(Material.GOLD_NUGGET, 1, "§e持っている数: " + kabuManager.getKabus(p) + "個");
		gui.setItem(21, tutorial);
		gui.setItem(22, kabus);
		gui.setItem(23, changes);
		p.openInventory(gui);
		viewers.add(p);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		if (p.getOpenInventory().getTopInventory() != null) {
			if (p.getOpenInventory().getTopInventory().getName().startsWith("§6§l株")) {
				event.setCancelled(true);
				if (event.getSlotType().equals(SlotType.OUTSIDE) || event.getCurrentItem().getType().equals(Material.AIR)) {
					return;
				}
				if (event.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
					int slot = event.getSlot();
					int amount = 0;
					boolean buy = true;
					if (slot == 11) {
						amount = 1;
						buy = true;
					} else if (slot == 12) {
						amount = 5;
						buy = true;
					} else if (slot == 13) {
						amount = 10;
						buy = true;
					} else if (slot == 14) {
						amount = 25;
						buy = true;
					} else if (slot == 15) {
						amount = 50;
						buy = true;
					} else if (slot == 29) {
						amount = 1;
						buy = false;
					} else if (slot == 30) {
						amount = 5;
						buy = false;
					} else if (slot == 31) {
						amount = 10;
						buy = false;
					} else if (slot == 32) {
						amount = 25;
						buy = false;
					} else if (slot == 33) {
						amount = 50;
						buy = false;
					}
					if (amount != 0) {
						if (buy) {
							if (kabuManager.buyKabu(p, amount)) {
								p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, (float) (1 + Math.random()));
							} else {
								int needed = (amount * kabuManager.getPrice()) - Coin.getCoin(p);
								p.sendMessage("§6" + needed + "§c円足りません");
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
							}
						} else {
							if (kabuManager.sellKabu(p, amount)) {
								p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, (float) (Math.random()));
							} else {
								int needed = amount - kabuManager.getKabus(p);
								p.sendMessage("§c株が§6" + needed + "§c個足りません");
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 0.5f);
							}
						}
						open(p);
					}
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();
		if (p.getOpenInventory().getTopInventory() != null) {
			if (p.getOpenInventory().getTopInventory().getName().startsWith("§6§l株")) {
				viewers.remove(p);
			}
		}
	}
}
