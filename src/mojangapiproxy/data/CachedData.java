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

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CachedData {

	private HashMap<String, HashMap<String, UUID>> data = new  HashMap<String, HashMap<String, UUID>>();

	public CachedData() {
		for (OfflinePlayer player : PlayersDataUtils.getPlayers()) {
			if (player.getName() == null) {
				continue;
			}
			addData(player.getName(), player.getUniqueId());
		}
	}

	private void putIfAbsent(String player) {
		if (!data.containsKey(player.toLowerCase())) {
			data.put(player.toLowerCase(), new HashMap<String, UUID>());
		}
	}

	public void addData(String name, UUID uuid) {
		putIfAbsent(name);
		data.get(name.toLowerCase()).put(name, uuid);
	}

	@SuppressWarnings("deprecation")
	public UUID getPlayerUUID(String name) {
		if (hasCaseMap(name)) {
			HashMap<String, UUID> casemap = getCaseMap(name);
			if (casemap.containsKey(name)) {
				return casemap.get(name);
			}
		}
		return Bukkit.getOfflinePlayer(name).getUniqueId();
	}

	private boolean hasCaseMap(String name) {
		return data.containsKey(name.toLowerCase());
	}

	private HashMap<String, UUID> getCaseMap(String name) {
		return data.get(name.toLowerCase());
	}

}
