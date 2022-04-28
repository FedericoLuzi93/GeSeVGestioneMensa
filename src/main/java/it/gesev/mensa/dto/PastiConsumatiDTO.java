package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastiConsumatiDTO 
{
	private Integer idPastiConsumati;
	private String dataPasto;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String cmd;
	private String tipoPersonale;
	private String oraIngresso;
	private TipoPagamentoDTO tipoPagamento;
	private TipoPastoDTO tipoPasto;
	private MensaDTO mensa;
	private IdentificativoSistemaDTO identificativoSistema;
	private TipoRazioneDTO tipoRazione;
}
