package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssReportRuoloMensaDTO 
{
	private Integer assReportRuoloMensaId;
	private String ruolo;
	private Integer idRuolo;
	private String etichetta;
	private Integer ordineFirma;
	private String flagApprovatore;
}
