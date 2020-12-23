package net.mycomp.mobivate;

public enum MobivateMessageClass {
  
	WELCOME_MESSAGE("WELCOME MESSAGE"),
	WELCOME_CONTENT("WELCOME CONTENT"),
	CONTENT("CONTENT");
	
	private String value;
	MobivateMessageClass(String value){
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
