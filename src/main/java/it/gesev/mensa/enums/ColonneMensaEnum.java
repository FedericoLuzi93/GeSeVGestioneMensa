package it.gesev.mensa.enums;

public enum ColonneMensaEnum 
{
	ENTE("descrizioneEnte"),
	DESCRIZIONE("descrizioneMensa"),
	TIPO_VETTOVAGLIAMENTO("codiceTipoFormaVettovagliamento");
	
	private String colonna;

	private ColonneMensaEnum(String colonna) 
	{
		this.colonna = colonna;
	}

	public String getColonna() 
	{
		return colonna;
	}
}
