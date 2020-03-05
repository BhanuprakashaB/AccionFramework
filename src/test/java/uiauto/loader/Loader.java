package uiauto.loader;

import uiauto.element.Identifier;

public interface Loader {

	Identifier getIdentifier(String name) throws Exception;
}
