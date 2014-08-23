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

package mojangapiproxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import mojangapiproxy.data.CachedData;
import mojangapiproxy.listeners.JoinListener;
import mojangapiproxy.proxy.ProxyInjector;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MojangAPIProxy extends JavaPlugin {

	private static MojangAPIProxy instance;
	public static MojangAPIProxy getMojangAPIProxy() {
		return instance;
	}

	private CachedData data;
	public CachedData getCachedData() {
		return data;
	}

	@Override
	public void onEnable() {
		instance = this;
		data = new CachedData();
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		try {
			ProxyInjector.injectProxy();
		} catch (Throwable t) {
			getLogger().severe("Unable to set proxy");
			t.printStackTrace();
			getLogger().severe("Shutting down server");
			Bukkit.shutdown();
		}
	}

	private HashSet<String> ignoredPlugins = new HashSet<String>(
		Arrays.asList(
			"SkinsRestorer"
		)
	);
	public boolean isPluginIgnored(Plugin plugin) {
		if (plugin == null) {
			return false;
		}
		return ignoredPlugins.contains(plugin.getName());
	}

	public Plugin getRequestingPlugin() {
		HashMap<ClassLoader, Plugin> map = getClassloaderToPluginMap();
		StackTraceElement[] stacktrace = new Exception().getStackTrace();
		for (int i = 0; i < stacktrace.length; i++) {
			StackTraceElement element = stacktrace[i];
			try {
				ClassLoader loader = Class.forName(element.getClassName(), false, getClass().getClassLoader()).getClassLoader();
				if (map.containsKey(loader)) {
					return map.get(loader);
				}
			} catch (ClassNotFoundException e) {
			}
		}
		return null;
	}

	private HashMap<ClassLoader, Plugin> getClassloaderToPluginMap() {
		HashMap<ClassLoader, Plugin> map = new HashMap<ClassLoader, Plugin>();
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			map.put(plugin.getClass().getClassLoader(), plugin);
		}
		map.remove(getClass().getClassLoader());
		return map;
	}

}
