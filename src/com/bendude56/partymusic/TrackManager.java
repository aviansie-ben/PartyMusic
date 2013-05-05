package com.bendude56.partymusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.getspout.spoutapi.SpoutManager;

public class TrackManager {
	private Properties musicList;
	
	public TrackManager() {
	}
	
	public TrackManager(String location) throws IOException {
		create(location);
		loadFrom(location);
	}
	
	public void create(String location) throws IOException {
		if (!new File(location).exists()) {
			new File(location).createNewFile();
		}
	}
	
	public void loadFrom(String location) throws IOException {
		musicList = new Properties();
		musicList.load(new FileInputStream(new File(location)));
		
		for (Map.Entry<Object, Object> track : musicList.entrySet()) {
			SpoutManager.getFileManager().addToCache(PartyMusic.instance, (String)track.getValue());
		}
	}
	
	public void removeTrack(String track) {
		SpoutManager.getFileManager().removeFromCache(PartyMusic.instance, getTrackLocation(track));
		
		musicList.remove(track);
	}
	
	public void setTrackLocation(String track, String location) {
		if (trackExists(track)) {
			SpoutManager.getFileManager().removeFromCache(PartyMusic.instance, getTrackLocation(track));
		}
		
		musicList.setProperty(track, location);
		
		SpoutManager.getFileManager().addToCache(PartyMusic.instance, location);
	}
	
	public String getTrackLocation(String track) {
		return musicList.getProperty(track);
	}
	
	public boolean trackExists(String track) {
		return musicList.containsKey(track.toLowerCase());
	}
	
	public void saveTo(String location) {
		try {
			musicList.store(new FileOutputStream(new File(location)), "");
		} catch (Exception e) {
			PartyMusic.instance.log.log(Level.SEVERE, "[PartyMusic] Failed to save music database:", e);
		}
	}
	
	public List<String> getTrackNames() {
		List<String> tracks = new ArrayList<String>();
		
		for (Object track : musicList.keySet()) {
			tracks.add((String)track);
		}
		
		return tracks;
	}
	
	public int getTrackCount() {
		return musicList.size();
	}
}
