package me.nucha.kokumin.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.nucha.kokumin.listeners.SelectSignListener;
import me.nucha.kokumin.utils.CustomItem;

public class CommandSelectSign implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("selectsign")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cプレイヤーのみ実行可能です");
				return true;
			}
			Player p = (Player) sender;
			CustomItem ci = new CustomItem(Material.BLAZE_ROD, 1, "看板セレクター", "看板をクリックしたあとに", "/setsign でテキストを設定できます");

			p.getInventory().addItem(ci);
			sender.sendMessage("§a看板セレクターを取得しました");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("setsign")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cプレイヤーのみ実行可能です");
				return true;
			}
			Player p = (Player) sender;
			if (args.length >= 1) {
				Sign s = null;
				if (!SelectSignListener.selected.containsKey(p)
						|| !(SelectSignListener.selected.get(p).getBlock().getState() instanceof Sign)) {
					sender.sendMessage("§c看板が見つかりません。先に選択していた場合は、破壊されている可能性があります");
					return true;
				} else {
					s = ((Sign) SelectSignListener.selected.get(p).getBlock().getState());
				}
				if (StringUtils.isNumeric(args[0])) {
					Integer line = Integer.valueOf(args[0]) - 1;
					if (line > 3) {
						line = 3;
					}
					if (line < 0) {
						line = 0;
					}
					if (args.length == 1) {
						s.setLine(line, " ");
						s.update();
						p.playSound(p.getLocation(), Sound.CLICK, 1, 1);
						sender.sendMessage("§a選択された看板の" + (line + 1) + "列目を削除しました");
						return true;
					}
					String text = args[1];
					if (args.length > 2) {
						for (int in = 2; in < args.length; in++) {
							text += " " + args[in];
						}
					}
					text = ChatColor.translateAlternateColorCodes('&', text);
					s.setLine(line, text);
					s.update();
					p.playSound(p.getLocation(), Sound.CLICK, 1, 1);
					sender.sendMessage("§a選択された看板の" + (line + 1) + "列目を\"" + text + "§a\"にしました");
					return true;
				} else {
					sender.sendMessage("§c数字を指定してください");
					return true;
				}
			}
			sender.sendMessage("/setsign <line> <text>");
			return true;
		}
		return true;
	}

}
