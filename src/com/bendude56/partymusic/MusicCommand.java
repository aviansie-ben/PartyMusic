package com.bendude56.partymusic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

public class MusicCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(PartyMusic.HEADER);
			sender.sendMessage(ChatColor.RED + "Proper use is: /music {play|stop|set|remove}");
		} else if (args[0].equalsIgnoreCase("play")) {
			if (!sender.hasPermission("partymusic.play")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music play <name>");
			} else {
				if (PartyMusic.instance.trackManager.trackExists(args[1])) {
					PartyMusic.instance.log.info("[PartyMusic] Playing " + args[1] + "...");
					for (Player p : Bukkit.getOnlinePlayers()) {
						SpoutManager.getSoundManager().playCustomMusic(PartyMusic.instance, SpoutManager.getPlayer(p), PartyMusic.instance.trackManager.getTrackLocation(args[1]), true);
					}
					sender.sendMessage(PartyMusic.HEADER);
					sender.sendMessage(ChatColor.GREEN + "Now playing " + args[1] + "!");
				} else {
					sender.sendMessage(PartyMusic.HEADER);
					sender.sendMessage(ChatColor.RED + "Music not found! Use /music set <name> <url> first!");
				}
			}
		} else if (args[0].equalsIgnoreCase("stop")) {
			if (!sender.hasPermission("partymusic.stop")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music stop");
			} else {
				PartyMusic.instance.log.info("[PartyMusic] Stopped playing");
				for (Player p : Bukkit.getOnlinePlayers()) {
					SpoutManager.getSoundManager().stopMusic(SpoutManager.getPlayer(p));
				}
				sender.sendMessage(PartyMusic.HEADER);
				sender.sendMessage(ChatColor.GREEN + "All music is now stopped!");
			}
		} else if (args[0].equalsIgnoreCase("set")) {
			if (!sender.hasPermission("partymusic.set")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 3) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music set <name> <url>");
			} else {
				PartyMusic.instance.log.info("[PartyMusic] Set " + args[1] + " to " + args[2]);
				
				PartyMusic.instance.trackManager.setTrackLocation(args[1], args[2]);
				PartyMusic.instance.trackManager.saveTo("plugins/PartyMusic/music.db");
				
				sender.sendMessage(PartyMusic.HEADER);
				sender.sendMessage(ChatColor.GREEN + args[1] + " has been successfully set to " + args[2] + "!");
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (!sender.hasPermission("partymusic.set")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music remove <name>");
			} else {
				PartyMusic.instance.log.info("[PartyMusic] Removed " + args[1]);
				
				PartyMusic.instance.trackManager.removeTrack(args[1]);
				PartyMusic.instance.trackManager.saveTo("plugins/PartyMusic/music.db");
				
				sender.sendMessage(PartyMusic.HEADER);
				sender.sendMessage(ChatColor.GREEN + args[1] + " has been successfully removed!");
			}
		} else if (args[0].equalsIgnoreCase("list")) {
			if (!sender.hasPermission("partymusic.list")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
			} else if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music list");
			} else {
				sender.sendMessage(PartyMusic.HEADER);
				if (PartyMusic.instance.trackManager.getTrackCount() == 0) {
					sender.sendMessage(ChatColor.RED + "There are no songs set!");
					sender.sendMessage(ChatColor.RED + "Use /music set <name> <url> to set one!");
				} else {
					String output = "";
					for(String track : PartyMusic.instance.trackManager.getTrackNames()) {
						if (count(output, ',') == 4) {
							sender.sendMessage(output + ChatColor.GREEN + track);
							output = "";
						} else {
							output += ChatColor.GREEN + track + ChatColor.GRAY + ", ";
						}
					}
					if (!output.isEmpty()) {
						sender.sendMessage(output.subSequence(0, output.length() - 4).toString());
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
