package me.nucha.kokumin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AltChecker {

	private static File altsYmlFile;
	private static FileConfiguration altsYml;
	private static Main plugin;

	public static void put(Player p) {
		String playername = p.getName();
		String uuid = p.getUniqueId().toString();
		String hostname = p.getAddress().getHostName();
		String hostaddress = p.getAddress().getAddress().getHostAddress();
		put(playername, uuid, hostname, hostaddress);
	}

	public static void put(String playername, String uuid, String hostname, String hostaddress) {
		altsYml.set(uuid + ".mcid", playername);
		List<String> hostaddresses = new ArrayList<String>();
		if (altsYml.isSet(uuid + ".ip.hostnames")) {
			hostaddresses = altsYml.getStringList(uuid + ".ip.hostnames");
			if (!hostaddresses.contains(hostname)) {
				hostaddresses.add(hostname);
				altsYml.set(uuid + ".ip.hostnames", hostaddresses);
			}
		} else {
			hostaddresses.add(hostname);
			altsYml.set(uuid + ".ip.hostnames", hostaddresses);
		}
		if (altsYml.isSet(uuid + ".ip.hostaddresses")) {
			hostaddresses = altsYml.getStringList(uuid + ".ip.hostaddresses");
			if (!hostaddresses.contains(hostaddress)) {
				hostaddresses.add(hostaddress);
				altsYml.set(uuid + ".ip.hostaddresses", hostaddresses);
			}
		} else {
			hostaddresses.add(hostaddress);
			altsYml.set(uuid + ".ip.hostaddresses", hostaddresses);
		}
	}

	public static boolean hasPut(Player p) {
		return altsYml.isSet(p.getUniqueId().toString());
	}

	public static List<String> getAltsByHostAddress(String hostaddress) {
		List<String> mcids = new ArrayList<String>();
		for (String uuid : altsYml.getKeys(false)) {
			if (altsYml.isSet(uuid + ".ip.hostaddresses")) {
				if (altsYml.getStringList(uuid + ".ip.hostaddresses").contains(hostaddress)) {
					mcids.add(altsYml.getString(uuid + ".mcid"));
				}
			}
		}
		return mcids;
	}

	public static List<String> getAltsByHostName(String hostname) {
		List<String> mcids = new ArrayList<String>();
		for (String uuid : altsYml.getKeys(false)) {
			if (altsYml.isSet(uuid + ".ip.hostnames")) {
				if (altsYml.getStringList(uuid + ".ip.hostnames").contains(hostname)) {
					mcids.add(altsYml.getString(uuid + ".mcid"));
				}
			}
		}
		return mcids;
	}

	public static List<String> getAltsByMCID(String mcid) {
		List<String> mcids = new ArrayList<String>();
		for (String uuid : altsYml.getKeys(false)) {
			if (altsYml.isSet(uuid + ".mcid") && altsYml.getString(uuid + ".mcid").equalsIgnoreCase(mcid)) {
				mcid = altsYml.getString(uuid + ".mcid");
				List<String> hostnames = getHostNamesByMCID(mcid);
				for (String hostname : hostnames) {
					List<String> alts = getAltsByHostName(hostname);
					for (String alt : alts) {
						if (!mcids.contains(alt)) {
							mcids.add(alt);
						}
					}
				}
				List<String> addresses = getAddressesByMCID(mcid);
				for (String address : addresses) {
					List<String> alts = getAltsByHostAddress(address);
					for (String alt : alts) {
						if (!mcids.contains(alt)) {
							mcids.add(alt);
						}
					}
				}
			}
		}
		return mcids;
	}

	public static List<String> getAddressesByMCID(String mcid) {
		for (String uuid : altsYml.getKeys(false)) {
			if (altsYml.isSet(uuid + ".mcid") && altsYml.getString(uuid + ".mcid").equalsIgnoreCase(mcid)) {
				return altsYml.getStringList(uuid + ".ip.addresses");
			}
		}
		return null;
	}

	public static List<String> getHostNamesByMCID(String mcid) {
		for (String uuid : altsYml.getKeys(false)) {
			if (altsYml.isSet(uuid + ".mcid") && altsYml.getString(uuid + ".mcid").equalsIgnoreCase(mcid)) {
				return altsYml.getStringList(uuid + ".ip.hostnames");
			}
		}
		return null;
	}

	public static String getUUIDByMCID(String mcid) {
		for (String uuid : altsYml.getKeys(false)) {
			if (altsYml.isSet(uuid + ".mcid") && altsYml.getString(uuid + ".mcid").equalsIgnoreCase(mcid)) {
				return uuid;
			}
		}
		return null;
	}

	public static String getMCIDbyUUID(String uuid) {
		return altsYml.getString(uuid + ".mcid");
	}

	public static void init() {
		plugin = Main.getInstance();
		altsYmlFile = new File(plugin.getDataFolder(), "alts.yml");
		if (!altsYmlFile.exists()) {
			plugin.saveResource("alts.yml", false);
		}
		altsYml = YamlConfiguration.loadConfiguration(altsYmlFile);
	}

	public static File getAltsYmlFile() {
		return altsYmlFile;
	}

	public static FileConfiguration getAltsYml() {
		return altsYml;
	}

	public static void save() {
		try {
			altsYml.save(altsYmlFile);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
