package me.nucha.kokumin.commands;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpeed implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("walkspeed")) {
			if (args.length >= 1) {
				if (StringUtils.isNumeric(args[0])) {
					double speed = Double.valueOf(args[0]);
					if (speed <= 0.0) {
						speed = 0.0;
					}
					if (speed >= 10.0) {
						speed = 10.0;
					}
					speed /= 10;
					BigDecimal bdSpeed = new BigDecimal(speed);
					bdSpeed.setScale(3, BigDecimal.ROUND_DOWN);
					double finalSpeed = bdSpeed.doubleValue();
					if (args.length == 1) {
						double before = p.getWalkSpeed();
						BigDecimal bdBefore = new BigDecimal(before);
						bdBefore = bdBefore.setScale(3, BigDecimal.ROUND_DOWN);
						double finalBefore = bdBefore.doubleValue();
						p.setWalkSpeed((float) speed);
						sender.sendMessage("§aWalkSpeedを§e" + finalBefore + "§aから§e" + finalSpeed + "§aにしました");
					} else if (args.length > 1) {
						Player t = Bukkit.getPlayer(args[0]);
						if (t != null) {
							double before = t.getWalkSpeed();
							BigDecimal bdBefore = new BigDecimal(before);
							bdBefore = bdBefore.setScale(3, BigDecimal.ROUND_DOWN);
							double finalBefore = bdBefore.doubleValue();
							t.setWalkSpeed((float) speed);
							sender.sendMessage("§a" + t.getName() + "のWalkSpeedを§e" + finalBefore + "§aから§e" + finalSpeed + "§aにしました");
						} else {
							sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりません");
						}
					}
				} else {
					sender.sendMessage("§cspeedは数字で指定してください");
				}
				return true;
			}
			sender.sendMessage("§cUsage: /walkspeed <speed>");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("flyspeed")) {
			if (args.length >= 1) {
				if (StringUtils.isNumeric(args[0])) {
					double speed = Double.valueOf(args[0]);
					if (speed <= 0.0) {
						speed = 0.0;
					}
					if (speed >= 10.0) {
						speed = 10.0;
					}
					speed /= 10;
					BigDecimal bdSpeed = new BigDecimal(speed);
					bdSpeed.setScale(3, BigDecimal.ROUND_DOWN);
					double finalSpeed = bdSpeed.doubleValue();
					if (args.length == 1) {
						double before = p.getWalkSpeed();
						BigDecimal bdBefore = new BigDecimal(before);
						bdBefore = bdBefore.setScale(3, BigDecimal.ROUND_DOWN);
						double finalBefore = bdBefore.doubleValue();
						p.setFlySpeed((float) speed);
						sender.sendMessage("§aFlySpeedを§e" + finalBefore + "§aから§e" + finalSpeed + "§aにしました");
					} else if (args.length > 1) {
						Player t = Bukkit.getPlayer(args[0]);
						if (t != null) {
							double before = t.getWalkSpeed();
							BigDecimal bdBefore = new BigDecimal(before);
							bdBefore = bdBefore.setScale(3, BigDecimal.ROUND_DOWN);
							double finalBefore = bdBefore.doubleValue();
							t.setFlySpeed((float) speed);
							sender.sendMessage("§a" + t.getName() + "のFlySpeedを§e" + finalBefore + "§aから§e" + finalSpeed + "§aにしました");
						} else {
							sender.sendMessage("§c" + args[0] + "というプレイヤーは見つかりません");
						}
					}
				} else {
					sender.sendMessage("§cspeedは数字で指定してください");
				}
				return true;
			}
			sender.sendMessage("§cUsage: /flyspeed <speed>");
			return true;
		}
		return true;
	}

}
