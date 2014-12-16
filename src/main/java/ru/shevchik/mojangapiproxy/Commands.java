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

import ru.shevchik.MojangAPIProxy.convertbackutils.WGConverter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
			if (args.length >= 2 && args[0].equalsIgnoreCase("convert")) {
				if (args[1].equalsIgnoreCase("wg")) {
					new Thread() {
						@Override
						public void run() {
							WGConverter.convertFromOnlineMode();
						}
					}.start();
					sender.sendMessage(ChatColor.YELLOW + "Started WorldGuard regions conversion");
					return true;
				}
			}
		}
		sender.sendMessage(ChatColor.RED + "Commands can be only used by console");
		return true;
	}

}
