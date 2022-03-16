package it.gesev.mensa.dto;

import java.util.List;

import lombok.Data;

@Data
public class FEMensaCompletaDTO 
{
		
	private MensaDTO mensaDTO;
	private List<FELocaliDTO> listaTipoLocaleDTO;
	private List<TipoPastoDTO> listaTipoPastoDTO;	
	private List<ServizioEventoDTO> listaServizioEventoDTO;
	private List<TipoDietaDTO> listaTipoDietaDTO;

}
