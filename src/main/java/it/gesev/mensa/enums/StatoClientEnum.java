package it.gesev.mensa.enums;

public enum StatoClientEnum {

	ATTESTATO("Attestato"),
	CONNESSO("Connesso"),
	DISCONNESSO("Disconnesso"),
	DISMESSO("Dismesso");
	
	private String statoClient;
	
	private StatoClientEnum(String statoClient)
	{
		this.statoClient = statoClient;
	}

	public String getStatoClient() {
		return statoClient;
	}
		
}
