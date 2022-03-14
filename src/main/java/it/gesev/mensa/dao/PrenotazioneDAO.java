package it.gesev.mensa.dao;

import java.util.List;

import it.gesev.mensa.dto.PrenotazioneDTO;

public interface PrenotazioneDAO 
{
	public void insertPrenotazione(List<PrenotazioneDTO> listaPrenotazioni);
}
