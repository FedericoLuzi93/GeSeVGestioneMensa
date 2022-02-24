package it.gesev.mensa.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DettaglioRuoloDTO implements Serializable
{

	private static final long serialVersionUID = -7646898736402726547L;
	private List<DipendenteDTO> listaDipendenti;
	private List<AssDipendenteRuoloDTO> associazioniRuolo;

}
