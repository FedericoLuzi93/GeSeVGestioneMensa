package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaricamentoPrenotazioniDTO 
{
	private Integer idPrenotazione;
	private String dataPrenotazione;
	private String codiceFiscale;
	private String flagCestino;
	private IdentificativoSistemaDTO identificativoSistema;
	private EnteDTO ente;
	private TipoPastoDTO tipoPasto;
	private TipoDietaDTO tipoDieta;
	private TipoRazioneDTO tipoRazione;
	
	
}
