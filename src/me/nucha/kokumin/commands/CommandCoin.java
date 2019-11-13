package me.nucha.kokumin.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nucha.kokumin.Main;
import me.nucha.kokumin.Premium;
import me.nucha.kokumin.coin.Coin;

public class CommandCoin implements CommandExecutor {

	private Main plugin;

	public CommandCoin(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("coin")) {
			if (args.length == 1) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (!Premium.isPremium(p)) {
						sender.sendMessage("§c他人のコインを見る権限は寄付の特典です。/donateで寄付を行ってください");
						return true;
					}
				}
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
						if (op.hasPlayedBefore()) {
							int coin = Coin.getCoin(op);
							sender.sendMessage("§b" + op.getName() + "§aさんは§e" + coin + "コイン持っています");
						} else {
							sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりませんでした");
						}
					}
				});
				return true;
			}
			if (sender instanceof Player) {
				Player p = (Player) sender;
				int coin = Coin.getCoin(p);
				sender.sendMessage("§aあなたは§e" + coin + "コイン持っています");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("addcoin")) {
			if (args.length == 2) {
				if (!StringUtils.isNumeric(args[1])) {
					sender.sendMessage("§c数字を入力してください");
					return true;
				}
				int addcoin = Integer.valueOf(args[1]);
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
						if (op.hasPlayedBefore()) {
							Coin.addCoin(op, addcoin, "運営からのプレゼント！");
							int coin = Coin.getCoin(op);
							sender.sendMessage("§b" + op.getName() + "§aさんに§e" + addcoin + "§aコインを与えました (現在§e" + coin + "§aコイン持っています)");
						} else {
							sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりませんでした");
						}
					}
				});
				return true;
			}
			sender.sendMessage("§a/addcoin <player> <coin>");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("takecoin")) {
			if (args.length == 2) {
				if (!StringUtils.isNumeric(args[1])) {
					sender.sendMessage("§c数字を入力してください");
					return true;
				}
				int addcoin = Integer.valueOf(args[1]);
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
						if (op.hasPlayedBefore()) {
							Coin.takeCoin(op, addcoin, "運営の怒り");
							int coin = Coin.getCoin(op);
							sender.sendMessage("§b" + op.getName() + "§cさんから§e" + addcoin + "§cコインを没収しました (現在§e" + coin + "§cコイン持っています)");
						} else {
							sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりませんでした");
						}
					}
				});
				return true;
			}
			sender.sendMessage("§a/addcoin <player> <coin>");
			return true;
		}
		return true;
	}

}
