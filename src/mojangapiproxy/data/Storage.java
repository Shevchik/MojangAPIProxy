package mojangapiproxy.data;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Storage {

	public static final Storage instance = new Storage();

	public static void init() {
	}

	private ConcurrentHashMap<String, PlayerProfile> data;

	public Storage() {
		OfflinePlayer[] players = Bukkit.getOfflinePlayers();
		data = new ConcurrentHashMap<String, PlayerProfile>(players.length);
		for (OfflinePlayer player : players) {
			if (player.getName() == null) {
				continue;
			}
			data.put(lc(player.getName()), profile(player));
		}
	}

	public void addPlayer(Player player) {
		data.put(lc(player.getName()), profile(player));
	}

	public PlayerProfile getProfile(String name) {
		return data.get(lc(name));
	}


	static PlayerProfile profile(OfflinePlayer player) {
		return new PlayerProfile(player.getName(), player.getUniqueId());
	}

	static String lc(String name) {
		return name.toLowerCase();
	}

}
