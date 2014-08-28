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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class ProxyStreamHandler extends URLStreamHandler {

	private final URLStreamHandler handler;
	private final Method openCon;
	private final Method openConProxy;

	public ProxyStreamHandler(String protocol) {
		if (protocol.equals("http")) {
			handler = new sun.net.www.protocol.http.Handler();
		} else {
			handler = new sun.net.www.protocol.https.Handler();
		}
		try {
			openCon = handler.getClass().getDeclaredMethod("openConnection", URL.class);
			openCon.setAccessible(true);
			openConProxy = handler.getClass().getDeclaredMethod("openConnection", URL.class, Proxy.class);
			openConProxy.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		if ((u.getHost().toLowerCase().equals("api.mojang.com") && u.getPath().toLowerCase().startsWith("/profiles/minecraft")) && shouldProxyRequest()) {
			return getProxyConnection(u, null);
		}
		return getDefaultConnection(u);
	}

	@Override
	protected URLConnection openConnection(URL u, Proxy p) throws IOException {
		if ((u.getHost().toLowerCase().equals("api.mojang.com") && u.getPath().toLowerCase().startsWith("/profiles/minecraft")) && shouldProxyRequest()) {
			return getProxyConnection(u, p);
		}
		return getDefaultConnection(u, p);
	}

	private URLConnection getProxyConnection(URL u, Proxy p) {
		return new ProxyConnection(u, p);
	}

	public URLConnection getDefaultConnection(URL u) {
		try {
			return (URLConnection) openCon.invoke(handler, u);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public URLConnection getDefaultConnection(URL u, Proxy p) {
		try {
			return (URLConnection) openConProxy.invoke(handler, u, p);
		} catch (IllegalAccessException |InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean shouldProxyRequest() {
		if (ProxyUtils.isPluginIgnored(ProxyUtils.getRequestingPlugin())) {
			return false;
		}
		return true;
	}

}
