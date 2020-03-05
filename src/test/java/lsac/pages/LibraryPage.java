package lsac.pages;

import uiauto.central.ContextConfiguration;
import uiauto.client.Client;
import uiauto.page.BasePage;

public class LibraryPage extends BasePage{

	public LibraryPage(ContextConfiguration conf, Client client) throws Exception {
		super("Library.json", conf, client);
	}
}
