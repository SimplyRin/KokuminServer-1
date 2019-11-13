package me.nucha.kokumin.commands;

import me.nucha.kokumin.Donation;
import me.nucha.kokumin.Main;
import me.nucha.kokumin.Premium;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDonation implements CommandExecutor {

	private Main plugin;

	public CommandDonation(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof BlockCommandSender) {
			if (args.length == 3) {
				if (StringUtils.isNumeric(args[2])) {
					OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
					if (args[1].equalsIgnoreCase("add")) {
						Donation.addDonation(op, Integer.valueOf(args[2]));
						sender.sendMessage("§e" + op.getName() + "§aに" + args[2] + "寄付ポイントを追加しました");
					}
					if (args[1].equalsIgnoreCase("take")) {
						Donation.takeDonation(op, Integer.valueOf(args[2]));
						sender.sendMessage("§e" + op.getName() + "§cから" + args[2] + "寄付ポイントを没収しました");
					}
				}
			}
			return true;
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!Premium.isPremium(p)) {
				sender.sendMessage("§cこのコマンドは、寄付の特典です。/donateで寄付を行ってください");
				return true;
			}
			if (args.length == 1) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					@Override
					public void run() {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
						if (op.hasPlayedBefore()) {
							int donation = Donation.getDonation(op);
							if (donation > 0) {
								sender.sendMessage("§d" + op.getName() + "§aさんは今までに§e" + donation + "円§a寄付しました");
							} else {
								sender.sendMessage("§d" + op.getName() + "§eさんは寄付をしていません");
							}
						} else {
							sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりませんでした");
						}
					}
				});
				return true;
			}
		}
		sender.sendMessage("§a/donation <player>");
		return true;
	}

}
