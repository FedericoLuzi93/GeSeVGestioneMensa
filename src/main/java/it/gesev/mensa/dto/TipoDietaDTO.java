package it.gesev.mensa.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.gesev.mensa.entity.TipoDieta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDietaDTO 
{
	private Integer idTipoDieta;
	private String descrizioneTipoDieta;
	private boolean flagNormale;
	
	public TipoDietaDTO fromEntityToDTO(TipoDieta tipoDieta) {
		if (Objects.nonNull(tipoDieta)) {
			TipoDietaDTO tipoDietaDTO = new TipoDietaDTO();
			tipoDietaDTO.setIdTipoDieta(tipoDieta.getIdTipoDieta());
			tipoDietaDTO.setDescrizioneTipoDieta(tipoDieta.getDescrizioneTipoDieta());
			tipoDietaDTO.setFlagNormale(true);
			return tipoDietaDTO;
		} else {
			return null;
		}
	}
}

