package it.gesev.mensa.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoFromaVettovagliamentoDTO 
{
	private Integer codiceTipoFormaVettovagliamento;
	private String descrizione;
	private List<MensaDTO> mensaDTO;

}
