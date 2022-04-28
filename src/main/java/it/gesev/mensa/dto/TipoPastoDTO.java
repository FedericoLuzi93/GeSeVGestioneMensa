package it.gesev.mensa.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.gesev.mensa.entity.TipoPasto;
import lombok.Data;

@Data
public class TipoPastoDTO 
{
	private Integer codiceTipoPasto;
	private String descrizione;
	
	private String orarioDal;
	private String orarioAl;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String oraFinePrenotazione;
	
	public TipoPastoDTO fromEntityToDTO(TipoPasto tipoPasto) {
		TipoPastoDTO tipoPastoDTO = new TipoPastoDTO();
		
		if (Objects.nonNull(tipoPasto)) {
			tipoPastoDTO.setCodiceTipoPasto(tipoPasto.getCodiceTipoPasto());
			tipoPastoDTO.setDescrizione(tipoPasto.getDescrizione());
			return tipoPastoDTO;
		} else {
			return null;
		}
	}
}
