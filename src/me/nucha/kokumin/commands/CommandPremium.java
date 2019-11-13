package me.nucha.kokumin.commands;

import me.nucha.kokumin.Main;
import me.nucha.kokumin.Premium;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandPremium implements CommandExecutor {

	private Main plugin;

	public CommandPremium(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			if (!sender.hasPermission("kokumin.premium.set")) {
				return true;
			}
			if (Bukkit.getOfflinePlayer(args[0]) != null) {
				OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
				if (args[1].equalsIgnoreCase("true")) {
					Premium.setPremium(op, true);
					sender.sendMessage("§e" + op.getName() + "§aはPremiumになりました！");
					if (op.isOnline()) {
						op.getPlayer().sendMessage("§a§lあなたはPremiumになりました！");
					}
				} else if (args[1].equalsIgnoreCase("false")) {
					Premium.setPremium(op, false);
					sender.sendMessage("§e" + op.getName() + "§cはPremiumではなくなりました！");
					if (op.isOnline()) {
						op.getPlayer().sendMessage("§c§lあなたはPremiumではなくなりました！");
					}
				} else {
					sender.sendMessage("§ctrueかfalseを指定してください");
				}
			} else {
				sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりませんでした");
			}
			return true;
		}
		if (args.length == 1) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				@Override
				public void run() {
					OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
					if (op.hasPlayedBefore()) {
						if (Premium.isPremium(op)) {
							sender.sendMessage("§a" + op.getName() + "はPremiumです");
						} else {
							sender.sendMessage("§c" + op.getName() + "はPremiumではありません");
						}
					} else {
						sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりませんでした");
					}
				}
			});
			return true;
		}
		String usage = "§a/premium <player>";
		if (sender.hasPermission("kokumin.premium.set")) {
			usage += " [true|false]";
		}
		sender.sendMessage(usage);
		return true;
	}

}
