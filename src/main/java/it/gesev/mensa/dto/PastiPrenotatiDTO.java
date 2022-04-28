package it.gesev.mensa.dto;

import it.gesev.mensa.entity.IdentificativoSistema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastiPrenotatiDTO
{
	
	private Integer idPrenotazione;
	private IdentificativoSistemaDTO identificativoSistema;
	private MensaDTO mensaDTO;
	private String dataPrenotazione;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String tipoPersonale;
	private GradoDTO gradoDTO;
	private TipoGradoDTO tipoGradoDTO;
	private StrutturaOrganizzativaDTO strutturaOrganizzativaDTO;
	private String denominazioneUnitaFunzionale;
	private String commensaleEsterno;
	private TipoPagamentoDTO tipoPagamentoDTO;
	private TipoPastoDTO tipoPastoDTO;
	private String flagCestino;
	private TipoDietaDTO tipoDietaDTP;
	private TipoRazioneDTO tipoRazioneDTO;
	private String specchioFlag;
	private String colObbligatoriaFlag;

}
