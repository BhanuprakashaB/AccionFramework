package uiauto.element;

import uiauto.lib.enums.ByType;

public class Identifier {
	private String idValue;
	private ByType idType;
	
	public Identifier(String idType, String idValue) throws Exception{
		this.idValue = idValue;
		this.idType = ByType.valueOf(idType.toUpperCase());
	}
	
	public String getIDValue(Object... inputs){
		return String.format(this.idValue, inputs);
	}
	
	public ByType getIDType(){
		return this.idType;
	}
}
