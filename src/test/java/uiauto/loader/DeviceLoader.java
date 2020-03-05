package uiauto.loader;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import uiauto.lib.enums.ConfigType;

public class DeviceLoader {
	
	public static synchronized Map<ConfigType, String> getDeviceInformations(String jsonFilePath, String deviceName) throws Exception{
		JsonReader reader = new JsonReader(new FileReader(jsonFilePath));
		JsonObject jsonObject = (new JsonParser()).parse(reader).getAsJsonObject();
		Map<ConfigType, String> map = new HashMap<ConfigType, String>();
		
		for(Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
			JsonObject value = (JsonObject) entry.getValue();

			if(entry.getKey().equalsIgnoreCase(deviceName)) {
				for(Map.Entry<String,JsonElement> entry1 : value.entrySet()) {
					ConfigType id = ConfigType.valueOf(entry1.getKey());
					String idValue = entry1.getValue().getAsString();
					try{
						map.put(id, idValue);
					} catch(IllegalArgumentException e) {
						throw new Exception(String.format("No enum constant in ConfigType.class for %s", id));
					}
				}	
			}
		}
		return map;
	}
}
