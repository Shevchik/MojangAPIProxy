/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package mojangapiproxy.data;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CachedData {

	private final ConcurrentHashMap<String, PlayerProfile> data;

	public CachedData() {
		List<OfflinePlayer> players = PlayersDataUtils.getPlayers();
		data = new ConcurrentHashMap<>(players.size() + Bukkit.getMaxPlayers());
		for (OfflinePlayer player : players) {
			if (player.getName() == null) {
				continue;
			}
			addData(player.getName(), player.getUniqueId());
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			addData(player.getName(), player.getUniqueId());
		}
	}

	public void addData(String name, UUID uuid) {
		data.put(name.toLowerCase(), new PlayerProfile(uuid, name));
	}

	public PlayerProfile getPlayerProfile(String name) {
		PlayerProfile cached = data.get(name.toLowerCase());
		if (cached != null) {
			return cached;
		}
		return new PlayerProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8)), name);
	}

	public static class PlayerProfile {

		private UUID uuid;
		private String name;

		public PlayerProfile(UUID uuid, String name) {
			this.uuid = uuid;
			this.name = name;
		}

		public UUID getUUID() {
			return uuid;
		}

		public String getName() {
			return name;
		}

	}

}
