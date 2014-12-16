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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import ru.shevchik.MojangAPIProxy.MojangAPIProxy;
import ru.shevchik.MojangAPIProxy.data.CachedData.PlayerProfile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.google.common.base.Charsets;

public class ProxyConnection extends HttpURLConnection {

	private final Proxy proxy;
	private ByteArrayInputStream inputStream;
	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private boolean outClosed = false;

	public ProxyConnection(URL url, Proxy proxy) {
		super(url);
		this.proxy = proxy;
	}

	@Override
	public void disconnect() {
	}

	@Override
	public boolean usingProxy() {
		return proxy != null;
	}

	@Override
	public void connect() throws IOException {
	}

	@Override
	public int getResponseCode() {
		return 200;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (inputStream == null) {
			outClosed = true;
			JsonArray users = new JsonParser().parse(new String(outputStream.toByteArray(), Charsets.UTF_8)).getAsJsonArray();
			StringBuilder reply = new StringBuilder("[");
			for (JsonElement user : users) {
				String username = user.getAsString();
				PlayerProfile info = MojangAPIProxy.getMojangAPIProxy().getCachedData().getPlayerProfile(username);
				reply.append("{");
				reply.append("\"id\":");
				reply.append("\"");
				reply.append(info.getUUID().toString().replace("-", ""));
				reply.append("\"");
				reply.append(",");
				reply.append("\"name\":");
				reply.append("\"");
				reply.append(info.getName());
				reply.append("\"");
				reply.append("}");
				reply.append(",");
			}
			reply.deleteCharAt(reply.length() - 1);
			reply.append("]");
			inputStream = new ByteArrayInputStream(reply.toString().getBytes(Charsets.UTF_8));
		}
		return inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if (outClosed) {
			throw new RuntimeException("Write after send");
		}
		return outputStream;
	}
}
