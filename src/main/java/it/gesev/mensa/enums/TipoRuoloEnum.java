package it.gesev.mensa.enums;

public enum TipoRuoloEnum 
{
	DIPENDENTE("DIPENDENTE"),
	ESTERNO("ESTERNO");
	
	private String tipoRuolo;
	
	private TipoRuoloEnum(String tipoRuolo)
	{
		this.tipoRuolo = tipoRuolo;
	}

	public String getTipoRuolo() {
		return tipoRuolo;
	}
}
