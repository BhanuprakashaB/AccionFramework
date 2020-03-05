package uiauto.client;

import uiauto.central.ContextConfiguration;
import uiauto.lib.enums.PlatformType;

public class ClientFactory {
	Client client;
	
	public Client getClient(PlatformType platformType, ContextConfiguration config, AppiumServer appiumServer) throws Exception {
		switch (platformType) {
		case MOBILE:
			client = new AppiumClient(config, appiumServer);
			break;
		case WEB:
			client = new SeleniumClient(config);
			break;
		}
		return client;
	}
}
