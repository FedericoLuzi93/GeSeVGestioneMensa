package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssDipendenteRuoloDTO implements Serializable
{

	private static final long serialVersionUID = 1816840466117453771L;
	private DipendenteDTO dipendente;
	private RuoloDTO ruolo;
	private Integer assDipendenteRuoloId;
	private OrganoDirettivoDTO organoDirettivo;
	private Integer idMensa;
	private String emailEsterno;
	private String nomeEsterno;
	private String cognomeEsterno;
	private String codiceFiscale;
	private Integer idFornitore;
}
