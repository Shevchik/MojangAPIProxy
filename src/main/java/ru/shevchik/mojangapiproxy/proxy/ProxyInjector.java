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

package ru.shevchik.MojangAPIProxy.proxy;

import java.lang.reflect.Field;
import java.net.URL;

public class ProxyInjector {

	public static void injectProxy() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ProxyFactory factory = new ProxyFactory();
		synchronized (getLock()) {
			Field factoryField = URL.class.getDeclaredField("factory");
			factoryField.setAccessible(true);
			factoryField.set(null, null);
		}
		URL.setURLStreamHandlerFactory(factory);
	}

	private static Object getLock() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field lockField = URL.class.getDeclaredField("streamHandlerLock");
		lockField.setAccessible(true);
		return lockField.get(null);
	}

}
