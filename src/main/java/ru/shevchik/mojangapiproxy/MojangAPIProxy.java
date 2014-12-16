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

package ru.shevchik.MojangAPIProxy;

import ru.shevchik.MojangAPIProxy.data.CachedData;
import ru.shevchik.MojangAPIProxy.proxy.ProxyInjector;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
		getServer().getPluginManager().registerEvents(new Listener()
		{
			@EventHandler(priority = EventPriority.LOWEST)
			public void onJoin(PlayerJoinEvent event)
			{
				getCachedData().addData(event.getPlayer().getName(), event.getPlayer().getUniqueId());
			}
		}, this);
		getCommand("mapiproxy").setExecutor(new Commands());
		try {
			ProxyInjector.injectProxy();
		} catch (Throwable t) {
			getLogger().severe("Unable to set proxy");
			t.printStackTrace();
			getLogger().severe("Shutting down server");
			Bukkit.shutdown();
		}
	}
	
	@Override
	public void onDisable() {
		getServer().getServicesManager().unregisterAll(this);
	}
	
}
