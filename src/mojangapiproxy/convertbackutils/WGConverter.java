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

package mojangapiproxy.convertbackutils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.domains.PlayerDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WGConverter {

	private static HashMap<UUID, String> cache = new HashMap<UUID, String>();

	public static void convertFromOnlineMode() {
		for (RegionManager rm : WGBukkit.getPlugin().getRegionContainer().getLoaded()) {
			for (ProtectedRegion pr : rm.getRegions().values()) {
				System.out.println("Converting region "+pr.getId());
				DefaultDomain ownersdd = pr.getOwners();
				PlayerDomain ownerspd = ownersdd.getPlayerDomain();
				for (UUID invalidUUID : new LinkedList<UUID>(ownerspd.getUniqueIds())) {
					if (invalidUUID.version() == 4) {
						String name = convertUUID(invalidUUID);
						if (name != null) {
							ownerspd.removePlayer(invalidUUID);
							ownerspd.addPlayer(name.toLowerCase());
						}
					}
				}
				pr.setOwners(ownersdd);

				DefaultDomain memberssdd = pr.getMembers();
				PlayerDomain memberspd = memberssdd.getPlayerDomain();
				for (UUID invalidUUID : new LinkedList<UUID>(memberspd.getUniqueIds())) {
					if (invalidUUID.version() == 4) {
						String name = convertUUID(invalidUUID);
						if (name != null) {
							memberspd.removePlayer(invalidUUID);
							memberspd.addPlayer(name.toLowerCase());
						}
					}
				}
				pr.setMembers(memberssdd);
				System.out.println("Converted region "+pr.getId());
			}
		}
		cache.clear();
	}

	private static String convertUUID(UUID uuid) {
		if (cache.containsKey(uuid)) {
			return cache.get(uuid);
		}
		String name = GetNameFromUUID.getName(uuid);
		if (name != null) {
			cache.put(uuid, name);
			return name;
		}
		return null;
	}

}
