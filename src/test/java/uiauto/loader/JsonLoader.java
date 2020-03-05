package uiauto.loader;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import uiauto.element.Identifier;

public class JsonLoader implements Loader {
	private Map<String, Identifier> map = new HashMap<String, Identifier>();

	public JsonLoader(String jsonFilePath, String devicePlatform) throws Exception {
		JsonReader reader = new JsonReader(new FileReader(jsonFilePath));
		JsonObject jsonObject = (new JsonParser()).parse(reader).getAsJsonObject();
		processIdentifiers(jsonObject, devicePlatform);
	}

	private void processIdentifiers(JsonObject jsonMap, String devicePlatform) throws Exception {

		for (Map.Entry<String, JsonElement> entry : jsonMap.entrySet()) {
			String key = entry.getKey();
			JsonObject value = (JsonObject) entry.getValue();

			for (Map.Entry<String, JsonElement> entry1 : value.entrySet()) {
				if (entry1.getKey().equalsIgnoreCase(devicePlatform)) {
					JsonObject value1 = (JsonObject) entry1.getValue();

					for (Map.Entry<String, JsonElement> entry2 : value1.entrySet()) {
						String id = entry2.getKey();
						String idValue = entry2.getValue().getAsString();
						Identifier identifier = new Identifier(id, idValue);
						this.map.put(key.toLowerCase(), identifier);
						break;
					}
				}
			}
		}
	}

	public Identifier getIdentifier(String name) throws Exception {
		return this.map.get(name.toLowerCase());
	}
}
