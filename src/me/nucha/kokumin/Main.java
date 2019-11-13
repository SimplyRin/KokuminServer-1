package me.nucha.kokumin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.nucha.kokumin.coin.Coin;
import me.nucha.kokumin.coin.CoinBooster;
import me.nucha.kokumin.commands.CommandCoin;
import me.nucha.kokumin.commands.CommandDonation;
import me.nucha.kokumin.commands.CommandMOTD;
import me.nucha.kokumin.commands.CommandOpenKabu;
import me.nucha.kokumin.commands.CommandPotionMaker;
import me.nucha.kokumin.commands.CommandPremium;
import me.nucha.kokumin.commands.CommandRename;
import me.nucha.kokumin.commands.CommandSelectSign;
import me.nucha.kokumin.commands.CommandSpeed;
import me.nucha.kokumin.commands.CommandTesting;
import me.nucha.kokumin.invitation.Invite;
import me.nucha.kokumin.invitation.InviteManager;
import me.nucha.kokumin.kabu.GuiKabu;
import me.nucha.kokumin.kabu.KabuManager;
import me.nucha.kokumin.listeners.BlockCommandListener;
import me.nucha.kokumin.listeners.PearlListener;
import me.nucha.kokumin.listeners.PlayerListener;
import me.nucha.kokumin.listeners.PotListener;
import me.nucha.kokumin.listeners.SelectSignListener;
import me.nucha.kokumin.listeners.gui.GuiPotionMaker;
import me.nucha.kokumin.utils.TimeUtil;
import me.nucha.kokumin.utils.Timestamputil;
import me.nucha.kokumin.utils.TitleUtil;

public class Main extends JavaPlugin implements Listener {

	private static Main plugin;
	private static boolean testing;
	private static boolean fastpot;
	private static String motd_comment;
	public static InviteManager inviteManager;
	public static KabuManager kabuManager;
	public static TitleUtil titleUtil;

	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		testing = false;
		fastpot = false;
		Premium.init(plugin);
		Coin.init(plugin);
		if (getConfig().isSet("motd_comment")) {
			motd_comment = getConfig().getString("motd_comment");
		} else {
			motd_comment = "&ePvP!";
			getConfig().set("motd_comment", motd_comment);
		}
		if (getConfig().isSet("fastpot")) {
			fastpot = getConfig().getBoolean("fastpot");
		} else {
			fastpot = true;
			getConfig().set("fastpot", fastpot);
		}
		saveConfig();
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new BukkitRunnable() {

			@Override
			public void run() {
				List<CoinBooster> boosters = new ArrayList<CoinBooster>();
				boosters.addAll(Coin.getActiveBoosters());
				for (CoinBooster booster : boosters) {
					booster.tick();
				}
			}

		}, 1L, 1L);
		AltChecker.init();
		inviteManager = new InviteManager(plugin);
		kabuManager = new KabuManager(plugin);
		GuiKabu.kabuManager(kabuManager);
		titleUtil = new TitleUtil();
		List<Listener> listeners = new ArrayList<Listener>();
		listeners.add(new GuiKabu(kabuManager));
		listeners.add(new PlayerListener());
		listeners.add(new PotListener());
		listeners.add(new PearlListener(this));
		listeners.add(new BlockCommandListener());
		listeners.add(new GuiPotionMaker());
		listeners.add(new SelectSignListener());
		listeners.add(this);
		PluginManager pm = getServer().getPluginManager();
		for (Listener listener : listeners) {
			pm.registerEvents(listener, this);
		}
		getCommand("premium").setExecutor(new CommandPremium(this));
		getCommand("donation").setExecutor(new CommandDonation(this));
		getCommand("coin").setExecutor(new CommandCoin(this));
		getCommand("addcoin").setExecutor(new CommandCoin(this));
		getCommand("takecoin").setExecutor(new CommandCoin(this));
		getCommand("setmotd").setExecutor(new CommandMOTD(this));
		getCommand("toggletesting").setExecutor(new CommandTesting());
		getCommand("walkspeed").setExecutor(new CommandSpeed());
		getCommand("flyspeed").setExecutor(new CommandSpeed());
		getCommand("rename").setExecutor(new CommandRename());
		getCommand("openkabu").setExecutor(new CommandOpenKabu());
		getCommand("potionmaker").setExecutor(new CommandPotionMaker());
		getCommand("selectsign").setExecutor(new CommandSelectSign());
		getCommand("setsign").setExecutor(new CommandSelectSign());
	}

	@Override
	public void onDisable() {
		PlayerListener.otoshidamaCheck.save();
		saveConfig();
		plugin = null;
		AltChecker.save();
		List<CoinBooster> cloneList = new ArrayList<>();
		for (CoinBooster booster : Coin.getActiveBoosters()) {
			cloneList.add(booster);
		}
		Coin.shutdown();
		for (CoinBooster booster : cloneList) {
			booster.expire();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch (cmd.getName()) {
		case "accountinfo":
			if (args.length == 1) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
					OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
					if (op != null && (op.hasPlayedBefore() || op.isOnline())) {
						List<String> alts_ = AltChecker.getAltsByMCID(args[0]);
						List<String> addresses_ = AltChecker.getAddressesByMCID(args[0]);
						List<String> hostnames_ = AltChecker.getHostNamesByMCID(args[0]);
						BanList addressBans = getServer().getBanList(Type.IP);
						BanList nameBans = getServer().getBanList(Type.NAME);
						List<String> alts = new ArrayList<String>();
						List<String> addresses = new ArrayList<String>();
						List<String> hostnames = new ArrayList<String>();
						for (String alt : alts_) {
							if (nameBans.isBanned(alt)) {
								alts.add("§8- §c" + alt);
							} else {
								alts.add("§8- §a" + alt);
							}
						}
						for (String address : addresses_) {
							if (addressBans.isBanned(address)) {
								addresses.add("§8- §c" + address);
							} else {
								addresses.add("§8- §a" + address);
							}
						}
						for (String hostname : hostnames_) {
							if (addressBans.isBanned(hostname)) {
								hostnames.add("§8- §c" + hostname);
							} else {
								hostnames.add("§8- §a" + hostname);
							}
						}
						sender.sendMessage("§b----------" + args[0] + "の情報 ----------");
						sender.sendMessage("§e§lサブアカウント一覧");
						for (String alt : alts) {
							sender.sendMessage(alt);
						}
						sender.sendMessage("§e§lIP §8§l- §e§lAddress & Hostname 一覧");
						for (String address : addresses) {
							sender.sendMessage(address);
						}
						for (String hostname : hostnames) {
							sender.sendMessage(hostname);
						}
					} else {
						sender.sendMessage("§c" + args[0] + "はログインしたことがありません");
					}
				});
				return true;
			}
			sender.sendMessage("§a/accinfo <player>");
			break;
		case "fastpot":
			if (fastpot) {
				sender.sendMessage("§eFast Potを無効化しました");
				fastpot = false;
			} else {
				sender.sendMessage("§aFast Potを有効化しました");
				fastpot = true;
			}
			getConfig().set("fastpot", fastpot);
			break;
		case "enderchest":
			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.openInventory(p.getEnderChest());
				p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 1);
			}
			break;
		case "kaboom":
			if (!Kaboom.canKaboom()) {
				sender.sendMessage("§cクールダウン中です");
				return true;
			}
			Kaboom.kaboom();
			break;
		case "boosteradmin":
			if (sender instanceof Player) {
				Player p = (Player) sender;
				String prefix = "§e[BoosterAdmin] §r";
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("boosters")) {
						for (CoinBooster booster : Coin.getActiveBoosters()) {
							Date expire = booster.getExpire();
							String till = TimeUtil.toDiffTime(System.currentTimeMillis(), expire.getTime(), "HH時間 mm分 ss秒");
							sender.sendMessage("§a" + booster.getOwner().getName() + "が有効化した x" + booster.getAmplifier() + " のコインブースター"
									+ " 期限: " + till);
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("deactivate")) {
						if (Coin.hasActivatedBooster(p)) {
							Coin.getActivatedBooster(p).expire();
						}
						return true;
					}
				}
				if (args.length == 4) {
					if (args[0].equalsIgnoreCase("activate")) {
						if (Coin.hasActivatedBooster(p)) {
							sender.sendMessage(prefix + "§cあなたにはアクティブなブースターがあります");
							return true;
						}
						int time = 0;
						double multiplier = Double.valueOf(args[3]);
						if (StringUtils.isNumeric(args[1])) {
							time = Integer.valueOf(args[1]);
						} else {
							sender.sendMessage(prefix + "§c数字を入力してください");
							return true;
						}
						if (!(multiplier > 1)) {
							sender.sendMessage(prefix + "§c1より大きい数字をいれてください");
							return true;
						}
						Date date = new Date();
						if (args[2].equalsIgnoreCase("h")) {
							date.setHours(date.getHours() + time);
						} else if (args[2].equalsIgnoreCase("m")) {
							date.setMinutes(date.getMinutes() + time);
						} else {
							sender.sendMessage(prefix + "§c時間の単位は h/m の中から選んで下さい");
							return true;
						}
						CoinBooster booster = new CoinBooster(multiplier, date, p);
						booster.activate();
						return true;
					}
				}
				sender.sendMessage("§e---------- Booster Admin Commands ----------");
				sender.sendMessage("§a/boosteradmin boosters");
				sender.sendMessage("§a/boosteradmin activate <time> <h/m> <multiplier>");
				sender.sendMessage("§a/boosteradmin deactivate");
			}
			break;
		case "invite":
			if (!(sender instanceof Player)) {
				return true;
			}
			Bukkit.getScheduler().runTaskAsynchronously(
					plugin,
					() -> {
						String prefix = "§2[§aInvites§2] §r";
						Player p = (Player) sender;
						if (args.length == 1) {
							if (args[0].equalsIgnoreCase("code")) {
								Invite invite = new Invite(p, plugin);
								if (invite.hasCreated()) {
									sender.sendMessage(prefix + "§eあなたは既に招待コードを生成しています");
									sender.sendMessage(prefix + "§b招待コードは " + invite.getCode() + " です");
									return;
								}
								if (invite.create()) {
									sender.sendMessage(prefix + "§d招待コードを生成しました！");
									sender.sendMessage(prefix + "§bコードは " + invite.getCode() + " です");
									sender.sendMessage(prefix + "§e招待したプレイヤーに §d/invite accept " + invite.getCode()
											+ " §eと打ってもらうことでコインを受け取ることができます");
									sender.sendMessage(prefix + "§eコマンドを打ったプレイヤーもコインをもらえます");
								} else {
									sender.sendMessage(prefix + "§cエラーが発生しました...もう一度お試しください");
								}
								return;
							}
							if (args[0].equalsIgnoreCase("record")) {
								Invite invite = new Invite(p, plugin);
								if (!invite.hasCreated()) {
									sender.sendMessage(prefix + "§cあなたはまだ招待コードを生成していません");
									sender.sendMessage(prefix + "§d/invite code §bで招待コードを生成できます");
									return;
								}
								if (!invite.hasAcceptedAtLeastOnce()) {
									sender.sendMessage(prefix + "§cあなたの招待コードは一度も使われていません");
									sender.sendMessage(prefix + "§d/invite accept " + invite.getCode() + " §bと打ってもらうことで§eコイン§bがもらえます");
									return;
								}
								List<String> accepts = invite.getAccepts();
								String names = "";
								boolean first = true;
								for (String uuid : accepts) {
									if (first) {
										names += AltChecker.getMCIDbyUUID(uuid);
										first = false;
										continue;
									}
									names += ", " + AltChecker.getMCIDbyUUID(uuid);
								}
								sender.sendMessage(prefix + "§a--- あなたの招待コードを使ったプレイヤー一覧 §a---");
								sender.sendMessage("§e" + names);
								return;
							}
						}
						if (args.length == 2) {
							if (args[0].equalsIgnoreCase("accept")) {
								String code = args[1];
								if (inviteManager.getInviteByCode(code) == null) {
									sender.sendMessage(prefix + "§cその招待コードは無効です");
									return;
								}
								if (inviteManager.hasAcceptedInvite(p.getUniqueId())) {
									sender.sendMessage(prefix + "§cあなたは既に招待コードを使用したことがあります");
									return;
								}
								Invite invite = inviteManager.getInviteByCode(code);
								if (invite.getOwner().getUniqueId().equals(p.getUniqueId())) {
									sender.sendMessage(prefix + "§c自分で自分を招待することはできません");
									return;
								}
								invite.accept(p.getUniqueId());
								sender.sendMessage(prefix + "§b§l招待コードを使用しました！");
								if (invite.getOwner().isOnline()) {
									invite.getOwner().getPlayer().sendMessage(prefix + "§b§l" + sender.getName() + "が招待コードを使用しました！");
								}
								Coin.addCoin(p, 500, "招待コードを使用 / Used an Invite Code");
								Coin.addCoin(invite.getOwner(), 50, "プレイヤーを招待 / Invited a Player");
								return;
							}
							if (args[0].equalsIgnoreCase("record")) {
								if (sender.isOp()) {
									OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
									if (!(op.isOnline() || op.hasPlayedBefore())) {
										sender.sendMessage(prefix + "§cプレイヤーが見つかりませんでした");
										return;
									}
									Invite invite = new Invite(op, plugin);
									if (!invite.hasCreated()) {
										sender.sendMessage(prefix + "§c" + op.getName() + "は招待コードを生成していません");
										return;
									}
									if (!invite.hasAcceptedAtLeastOnce()) {
										sender.sendMessage(prefix + "§c" + op.getName() + "の招待コードは一度も使われていません");
										return;
									}
									List<String> accepts = invite.getAccepts();
									String names = "";
									boolean first = true;
									for (String uuid : accepts) {
										if (first) {
											names += AltChecker.getMCIDbyUUID(uuid);
											first = false;
											continue;
										}
										names += ", " + AltChecker.getMCIDbyUUID(uuid);
									}
									sender.sendMessage("§a--- §2" + op.getName() + "の招待コードを使ったプレイヤー一覧 §a---");
									sender.sendMessage("§e" + names);
									return;
								}
							}
							if (args[0].equalsIgnoreCase("code")) {
								if (sender.isOp()) {
									OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
									if (!(op.isOnline() || op.hasPlayedBefore())) {
										sender.sendMessage(prefix + "§cプレイヤーが見つかりませんでした");
										return;
									}
									Invite invite = new Invite(op, plugin);
									if (invite.hasCreated()) {
										sender.sendMessage(prefix + "§b" + op.getName() + "の招待コードは " + invite.getCode() + " です");
									} else {
										sender.sendMessage(prefix + "§c" + op.getName() + "は招待コードを生成していません");
									}
									return;
								}
							}
						}
						sender.sendMessage(prefix + "§a---------- Invitation Commands ----------");
						sender.sendMessage(prefix + "§e/invite code §8- §6招待コードを生成(または取得)します");
						sender.sendMessage(prefix + "§e/invite accept <code> §8- §6招待コードを使用します");
						sender.sendMessage(prefix + "§e/invite record §8- §6招待コードの記録を取得します");
						if (sender.isOp()) {
							sender.sendMessage(prefix + "§b/invite code <player> §8- §6<player>の招待コードを取得します");
							sender.sendMessage(prefix + "§b/invite record <player> §8- §6<player>の招待コードの記録を取得します");
						}
					});
			break;
		case "system":
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("runtime")) {
					DecimalFormat f1 = new DecimalFormat("#,###KB");
					DecimalFormat f2 = new DecimalFormat("##.#");
					long free = Runtime.getRuntime().freeMemory() / 1024;
					long total = Runtime.getRuntime().totalMemory() / 1024;
					long max = Runtime.getRuntime().maxMemory() / 1024;
					long used = total - free;
					double ratio = (used * 100 / (double) total);
					String info = "合計: " + f1.format(total) + "\n" + "使用量: " + f1.format(used) + " (" + f2.format(ratio) + "%)\n"
							+ "使用可能最大: " + f1.format(max);
					sender.sendMessage(info);
					return true;
				}
			}
			sender.sendMessage("§a/system runtime");
			break;
		case "namehistory":
			if (args.length == 1) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					@Override
					public void run() {
						try {
							OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
							UUID uuid = op.getUniqueId();
							String mcid = op.getName();
							sender.sendMessage("§d------------ " + mcid + " ------------");
							List<JSONObject> json = getNameHistory(uuid);
							boolean a = true;
							for (JSONObject jo : json) {
								String name = (String) jo.get("name");
								if (!jo.containsKey("changedToAt")) {
									sender.sendMessage("§e" + name + " §8- §e" + "最初のID");
								} else {
									long changedToAtLong = (long) jo.get("changedToAt");
									String TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
									String changedToAt = Timestamputil.formattedTimestamp(new Timestamp(changedToAtLong), TIME_FORMAT);
									if (a) {
										sender.sendMessage("§a" + name + " §8- §a" + changedToAt);
										a = false;
									} else {
										sender.sendMessage("§2" + name + " §8- §2" + changedToAt);
										a = true;
									}
								}
							}
						} catch (NullPointerException e) {
							sender.sendMessage("§cError: " + e.getLocalizedMessage());
						}
					}
				});
				return true;
			}
			sender.sendMessage("§a/namehistory <name> - §aプレイヤーのID変更履歴を表示します");
			break;
		case "ping":
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage("§6" + args[0] + " §6はオフラインです");
					return true;
				}
				int ping = ((CraftPlayer) target).getHandle().ping;
				sender.sendMessage("§8[§aPing§8] §f" + target.getDisplayName() + " §a: " + ping + "ms");
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage("プレイヤーのみ実行可能です");
				return true;
			}
			Player p = (Player) sender;
			int ping = ((CraftPlayer) p).getHandle().ping;
			sender.sendMessage("§8[§aPing§8] §f" + p.getDisplayName() + " §a: " + ping + "ms");
			break;
		default:
			break;
		}
		return true;
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if (testing) {
			if (event.getCause() == TeleportCause.ENDER_PEARL) {
				Location to = event.getTo();
				if (to.getBlock().getType() != Material.AIR) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDrink(PlayerItemConsumeEvent event) {
		Player p = event.getPlayer();
		ItemStack item = event.getItem();
		if (item.getType().equals(Material.POTION) && item.getAmount() == 1) {
			Bukkit.getScheduler().runTask(plugin, new Runnable() {
				public void run() {
					p.setItemInHand(new ItemStack(Material.AIR));
				}
			});
		}
	}

	public static String getMotdComment() {
		return motd_comment;
	}

	public static void setMotdComment(String motd_comment) {
		Main.motd_comment = motd_comment;
	}

	public static boolean isFastPotEnabled() {
		return fastpot;
	}

	public static boolean isTesting() {
		return testing;
	}

	public static void setTesting(boolean testing) {
		Main.testing = testing;
	}

	public static Main getInstance() {
		return plugin;
	}

	public List<JSONObject> getNameHistory(UUID uuid) { // victim name | ban date | banned by | banned until | reason
		List<JSONObject> jsonObjects = new ArrayList<>();
		try {
			String uid = uuid.toString().replaceAll("-", "");
			URL url = new URL("https://api.mojang.com/user/profiles/" + uid + "/names");
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-agent", "Mozilla/5.0");
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader inb = new BufferedReader(in);
			String line = inb.readLine();
			String[] split = line.replace("[", "").replace("]", "").replace("},{", "}},{{").split(Pattern.quote("},{"));
			for (String parse : split) {
				JSONObject jsonObject;
				try {
					jsonObject = (JSONObject) new JSONParser().parse(parse);
					jsonObjects.add(jsonObject);
				} catch (ParseException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
			inb.close();
			in.close();
			return jsonObjects;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
