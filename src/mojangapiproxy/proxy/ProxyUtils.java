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

package mojangapiproxy.proxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import mojangapiproxy.MojangAPIProxy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ProxyUtils {

	private static final HashSet<String> ignoredPlugins = new HashSet<String>(
		Arrays.asList(
			"SkinsRestorer"
		)
	);

	public static void addIgnoredPlugin(Plugin plugin) {
		ignoredPlugins.add(plugin.getName());
	}

	public static boolean isPluginIgnored(Plugin plugin) {
		if (plugin == null) {
			return true;
		}
		return ignoredPlugins.contains(plugin.getName());
	}

	public static Plugin getRequestingPlugin() {
		HashMap<ClassLoader, Plugin> map = getClassloaderToPluginMap();
		StackTraceElement[] stacktrace = new Exception().getStackTrace();
		for (int i = 0; i < stacktrace.length; i++) {
			StackTraceElement element = stacktrace[i];
			try {
				ClassLoader loader = Class.forName(element.getClassName(), false, MojangAPIProxy.class.getClassLoader()).getClassLoader();
				if (map.containsKey(loader)) {
					return map.get(loader);
				}
			} catch (ClassNotFoundException e) {
			}
		}
		return null;
	}

	private static HashMap<ClassLoader, Plugin> getClassloaderToPluginMap() {
		HashMap<ClassLoader, Plugin> map = new HashMap<ClassLoader, Plugin>();
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			map.put(plugin.getClass().getClassLoader(), plugin);
		}
		map.remove(MojangAPIProxy.class.getClassLoader());
		return map;
	}

}
