package it.gesev.mensa.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class AssTipoPastoMensaDTO
{
	private Integer assTipoPastoMensaId;
	private LocalTime orarioDal;
	private LocalTime orarioAl;
	private LocalTime oraFinePrenotazione;
	private MensaDTO mensaDTO;
	private TipoPastoDTO tipoPastoDTO;
	
}
