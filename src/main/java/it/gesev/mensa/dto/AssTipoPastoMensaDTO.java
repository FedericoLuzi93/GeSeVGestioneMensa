package it.gesev.mensa.dto;

import lombok.Data;

@Data
public class AssTipoPastoMensaDTO
{
	private Integer assTipoPastoMensaId;
	private String orarioDal;
	private String orarioAl;
	private String oraFinePrenotazione;
	private MensaDTO mensaDTO;
	private TipoPastoDTO tipoPastoDTO;
	
}
