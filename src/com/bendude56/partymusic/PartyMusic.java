package com.bendude56.partymusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;

import com.bendude56.bencmd.User;

public class PartyMusic extends JavaPlugin {
	
	public static final String HEADER = ChatColor.GREEN + "~--~" + ChatColor.YELLOW + " PartyMusic " + ChatColor.GREEN + "~--~";
	
	public Logger log = Logger.getLogger("Minecraft");
	public Properties list;

	@Override
	public void onDisable() {
		list = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			SpoutManager.getSoundManager().stopMusic(SpoutManager.getPlayer(p));
		}
		log.info("[PartyMusic] Disabled...");
	}

	@Override
	public void onEnable() {
		try {
			list = new Properties();
			if (!new File("plugins/BenCmd/music.db").exists()) {
				new File("plugins/BenCmd/music.db").createNewFile();
			}
			list.load(new FileInputStream(new File("plugins/BenCmd/music.db")));
			log.info("[PartyMusic] Ready to play!");
		} catch (Exception e) {
			log.log(Level.SEVERE, "[PartyMusic] Something broke!", e);
		}
	}
	
	public boolean BCE() {
		return Bukkit.getServer().getPluginManager().isPluginEnabled("BenCmd");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		User user = User.getUser(sender);
		if (args.length == 0) {
			sender.sendMessage(HEADER);
			sender.sendMessage(ChatColor.RED + "Proper use is: /music {play|stop|set|remove}");
		} else if (args[0].equalsIgnoreCase("play")) {
			if (!((BCE() && user.hasPerm("pmusic.play")) || sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music play <name>");
			} else {
				if (list.containsKey(args[1])) {
					log.info("[PartyMusic] Playing " + args[1] + "...");
					for (Player p : Bukkit.getOnlinePlayers()) {
						SpoutManager.getSoundManager().playCustomMusic(this, SpoutManager.getPlayer(p), list.getProperty(args[1]), true);
					}
					sender.sendMessage(HEADER);
					sender.sendMessage(ChatColor.GREEN + "Now playing " + args[1] + "!");
				} else {
					sender.sendMessage(HEADER);
					sender.sendMessage(ChatColor.RED + "Music not found! Use /music set <name> <url> first!");
				}
			}
		} else if (args[0].equalsIgnoreCase("stop")) {
			if (!((BCE() && user.hasPerm("pmusic.stop")) || sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music stop");
			} else {
				log.info("[PartyMusic] Stopped playing");
				for (Player p : Bukkit.getOnlinePlayers()) {
					SpoutManager.getSoundManager().stopMusic(SpoutManager.getPlayer(p));
				}
				sender.sendMessage(HEADER);
				sender.sendMessage(ChatColor.GREEN + "All music is now stopped!");
			}
		} else if (args[0].equalsIgnoreCase("set")) {
			if (!((BCE() && user.hasPerm("pmusic.set")) || sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 3) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music set <name> <url>");
			} else {
				log.info("[PartyMusic] Set " + args[1] + " to " + args[2]);
				list.setProperty(args[1], args[2]);
				try {
					list.store(new FileOutputStream(new File("plugins/BenCmd/music.db")), "");
				} catch (Exception e) {
					log.log(Level.SEVERE, "[PartyMusic] Failed to save music database:", e);
				}
				sender.sendMessage(HEADER);
				sender.sendMessage(ChatColor.GREEN + args[1] + " has been successfully set to " + args[2] + "!");
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (!((BCE() && user.hasPerm("pmusic.set")) || sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music remove <name>");
			} else {
				log.info("[PartyMusic] Removed " + args[1]);
				list.remove(args[1]);
				try {
					list.store(new FileOutputStream(new File("plugins/BenCmd/music.db")), "");
				} catch (Exception e) {
					log.log(Level.SEVERE, "[PartyMusic] Failed to save music database:", e);
				}
				sender.sendMessage(HEADER);
				sender.sendMessage(ChatColor.GREEN + args[1] + " has been successfully removed!");
			}
		} else if (args[0].equalsIgnoreCase("list")) {
			if (!((BCE() && user.hasPerm("pmusic.list")) || sender.isOp())) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music list");
			} else {
				sender.sendMessage(HEADER);
				if (list.size() == 0) {
					sender.sendMessage(ChatColor.RED + "There are no songs set!");
					sender.sendMessage(ChatColor.RED + "Use /song set <name> <url> to set one!");
				} else {
					String output = "";
					for(Object key : list.keySet()) {
						if (count(output, ',') == 4) {
							sender.sendMessage(output + ChatColor.GREEN + ((String)key));
							output = "";
						} else {
							output += ChatColor.GREEN + ((String) key) + ChatColor.GRAY + ", ";
						}
					}
					if (!output.isEmpty()) {
						sender.sendMessage(output.subSequence(0, output.length() - 3).toString());
					}
				}
			}
		}
		return true;
	}
	
	public static int count(String str, char c) {
		int count = 0;
		for (char c2 : str.toCharArray()) {
			if (c2 == c) {
				count++;
			}
		}
		return count;
	}

}
