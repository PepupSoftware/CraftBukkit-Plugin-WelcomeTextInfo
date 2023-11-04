package de.pepupsoftware.craftbukkit.plugin.welcome;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PlayerTimeStore {

	private Map < String, Long > playerstime;
    private String dname;
    
	public PlayerTimeStore() {
	
		playerstime = new HashMap<String, Long>();
		dname = "PlayerTimeStore";
		load();
		
	}
	
	public PlayerTimeStore(String dateiname) {
		
		playerstime = new HashMap<String, Long>();
		dname = dateiname;
		load();
		
	}
	
	public void updatePlayer(Player p, long seconds) {

		try {
			if (playerstime.containsKey(p.getUniqueId().toString()) == true) {
				playerstime.put(p.getUniqueId().toString(), (long)playerstime.get(p.getUniqueId().toString()) + seconds);
			} else {
				playerstime.put(p.getUniqueId().toString(), seconds);
			}
		} catch(Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());			
		}
	}
	
	public long getPlayTimeSecond(Player p) {

		long seconds = 0;
		
		try {
			if (playerstime.containsKey(p.getUniqueId().toString()) == true) {
				seconds = playerstime.get(p.getUniqueId().toString());
			}
		} catch(Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());			
		}
		
		return(seconds);
	}
	
	@SuppressWarnings("unchecked")
	public void save() {
		
		try {
		
			JSONObject spielerliste = new JSONObject();
			
			for (Entry<String, Long> zeile : playerstime.entrySet()) {
				spielerliste.put(zeile.getKey(), zeile.getValue());				
			}
			
			FileWriter fw = new FileWriter(dname + ".json");
			spielerliste.writeJSONString(fw);
			fw.close();
			
		} catch(Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());			
		}
				 
	}
	
	private void load() {
		
		try {
		
			JSONParser parser = new JSONParser();
			FileReader fr = new FileReader(dname + ".json");
			JSONObject spielerliste = (JSONObject) parser.parse(fr);
			fr.close();

			playerstime.clear();
            @SuppressWarnings("unchecked")
			Iterator<String> keys = spielerliste.keySet().iterator();
            while (keys.hasNext()) {
            	String key = keys.next();
            	Long seconds = (Long) spielerliste.get(key);
            	playerstime.put(key, seconds);

            }
				
		} catch(FileNotFoundException e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Die Datei " + dname + ".json ist nicht vorhanden.");			
		} catch(Exception e) {
    		System.out.println("[WelcomeTextInfo-Plugin] Fehler: " + e.toString());			
		}
		
	}
}
