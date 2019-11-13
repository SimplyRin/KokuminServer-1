package me.nucha.kokumin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import me.nucha.kokumin.AltChecker;
import me.nucha.kokumin.Kaboom;
import me.nucha.kokumin.Main;
import me.nucha.kokumin.Premium;
import me.nucha.kokumin.otoshidama.OtoshidamaCheck;

public class PlayerListener implements Listener {

	private static Main plugin;
	public static OtoshidamaCheck otoshidamaCheck;

	public PlayerListener() {
		plugin = Main.getInstance();
		otoshidamaCheck = new OtoshidamaCheck(plugin);
	}

	@EventHandler
	public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
		if (!plugin.getConfig().isSet(event.getUniqueId() + ".donation")) {
			plugin.getConfig().set(event.getUniqueId() + ".donation", 0);
		}
		if (!plugin.getConfig().isSet(event.getUniqueId() + ".coin")) {
			plugin.getConfig().set(event.getUniqueId() + ".coin", 0);
		}
		AltChecker.put(event.getName(), event.getUniqueId().toString(), event.getAddress().getHostName(),
				event.getAddress().getHostAddress());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (otoshidamaCheck.isAvailable())
			otoshidamaCheck.claim(p);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			if (!plugin.getConfig().isSet(p.getUniqueId() + ".premium")) {
				Premium.setPremium(p, false);
			}
		});
	}

	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		String motd = Main.getMotdComment();
		if (Bukkit.getServer().hasWhitelist()) {
			motd = "&cメンテナンス中";
		}
		event.setMotd(event.getMotd() + " " + ChatColor.translateAlternateColorCodes('&', motd));
	}

	/*@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		Material type = event.getItemDrop().getItemStack().getType();
		if (type == Material.WOOD_SWORD || type == Material.STONE_SWORD || type == Material.IRON_SWORD || type == Material.GOLD_SWORD
				|| type == Material.DIAMOND_SWORD) {
			event.setCancelled(true);
		}
	}*/

	@EventHandler
	public void onFall(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (event.getCause() == DamageCause.FALL && Kaboom.hasNoFall(p)) {
				event.setCancelled(true);
				Kaboom.unregisterNoFall(p);
			}
		}
	}

}
