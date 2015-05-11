package mojangapiproxy.data;

import java.util.UUID;

public class PlayerProfile {
	private String name;
	private UUID uuid;
	public PlayerProfile(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}
}