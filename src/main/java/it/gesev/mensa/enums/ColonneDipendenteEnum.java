package it.gesev.mensa.enums;

public enum ColonneDipendenteEnum 
{
	NOME("nome"),
	COGNOME("cognome"),
	CODICE_FISCALE("codiceFiscale"),
	TIPO_PERSONALE("tipoPersonale"),
	GRADO("grado");
	
	private String colonna;
	
	private ColonneDipendenteEnum(String colonna)
	{
		this.colonna = colonna;
	}

	public String getColonna() {
		return colonna;
	}
	
	
	
}
