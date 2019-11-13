package me.nucha.kokumin.listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SelectSignListener implements Listener {

	public static HashMap<Player, Location> selected = new HashMap<>();

	public SelectSignListener() {
		selected = new HashMap<>();
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getClickedBlock().getState() instanceof Sign && p.getItemInHand() != null && p.getItemInHand().hasItemMeta()
					&& p.getItemInHand().getType().equals(Material.BLAZE_ROD) && p.getItemInHand().getItemMeta().hasDisplayName()
					&& p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("看板セレクター")) {
				Sign s = (Sign) event.getClickedBlock().getState();
				selected.put(p, s.getLocation());
				p.sendMessage("§a看板を選択しました");
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (selected.containsKey(p)) {
			selected.remove(p);
		}
	}

}
