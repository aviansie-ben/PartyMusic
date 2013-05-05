package com.bendude56.partymusic;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;

public class PartyMusic extends JavaPlugin {
	
	public static final String HEADER = ChatColor.GREEN + "~--~" + ChatColor.YELLOW + " PartyMusic " + ChatColor.GREEN + "~--~";
	public static PartyMusic instance;
	
	public Logger log = Logger.getLogger("Minecraft");
	public TrackManager trackManager;

	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			SpoutManager.getSoundManager().stopMusic(SpoutManager.getPlayer(p));
		}
		log.info("[PartyMusic] Disabled...");
	}

	@Override
	public void onEnable() {
		instance = this;
		try {
			new File("plugins/PartyMusic").mkdirs();
			
			trackManager = new TrackManager("plugins/PartyMusic/music.db");
			
			getCommand("music").setExecutor(new MusicCommand());
			
			log.info("[PartyMusic] Ready to play!");
		} catch (Exception e) {
			log.log(Level.SEVERE, "[PartyMusic] Something broke!", e);
		}
	}
}
