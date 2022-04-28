package it.gesev.mensa.enums;

public enum RuoloMensaEnum {

	RAPPRESENTANTE("Rappresentante della ditta appaltatrice"),
	OPERATORE("Operatore mensa"),
	CAPO_SERVIZIO_COMMISSARIATO("Capo servizio commissariato"),
	COMMISSARIATO("Commissariato"),
	UFFICIALE_SERVIZI_COMMISSARIATO("Ufficiale Addetto ai servizi di Commissariato"),
	SOTTUF_VETT("Sottufficiale addetto al vettovagliamento"),
	GESTORE("Gestore"),
	CAPO_SEGRETERIA("Capo segreteria"),
	CAPO_UFF_MAGG("Capo Ufficio Maggiorità e Personale"),
	COMANDANTE_CORPO("Comandante di Corpo"),
	CAPO_GEST_PATRIMONIALE("Capo Gestione Patrimoniale"),
	CAPO_SERV_AMM("Capo Servizio amministrativo"),
	CAPO_SEZ_COORD_AMM("Capo Sezione Coordinamento Amministrativo"),
	PRESIDENTE_SEZ_COORD_AMM("Presidente della commissione amministrativa"),
	AIUTANTE_GESTORE("Aiutante gestore");
	
	private String tipoRuolo;
	
	private RuoloMensaEnum(String tipoRuolo) {
		this.tipoRuolo = tipoRuolo;
	}

	public String getTipoRuolo() {
		return tipoRuolo;
	}
}
