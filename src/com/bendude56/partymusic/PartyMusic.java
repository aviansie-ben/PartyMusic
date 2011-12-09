package com.bendude56.partymusic;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyMusic extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft");
	public Properties list;

	@Override
	public void onDisable() {
		list = null;
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

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Proper use is: /music {play|stop|new}");
		} else if (args[0].equalsIgnoreCase("play")) {
			if (args.length == 1) {
				sender.sendMessage(ChatColor.RED + "Proper use is: /music play <name>");
			}
		}
		return true;
	}

}
