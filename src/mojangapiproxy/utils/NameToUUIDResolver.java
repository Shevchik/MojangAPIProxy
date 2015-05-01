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

package mojangapiproxy.utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class NameToUUIDResolver {

	public static PlayerProfile getPlayerProfile(String name) {
		UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
		return new PlayerProfile(uuid, name);
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
