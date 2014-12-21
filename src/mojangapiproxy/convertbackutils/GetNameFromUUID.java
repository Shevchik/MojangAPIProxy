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

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.base.Charsets;

public class GetNameFromUUID {

	private static String skullbloburl = "https://sessionserver.mojang.com/session/minecraft/profile/";
	public static String getName(UUID id) {
		try {
			URL url = new URL(skullbloburl+id.toString().replace("-", "")+"?unsigned=false");
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setUseCaches(false);
			InputStream is = connection.getInputStream();
			String result = IOUtils.toString(is, Charsets.UTF_8);
			IOUtils.closeQuietly(is);
			return (String) ((JSONObject) new JSONParser().parse(result)).get("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
