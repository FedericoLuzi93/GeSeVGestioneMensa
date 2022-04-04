package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.CaricamentoPrenotazioniDTO;
import it.gesev.mensa.dto.PrenotazioneDTO;

public interface PrenotazioneDAO 
{
	public void insertPrenotazione(List<CaricamentoPrenotazioniDTO> listaPrenotazioni);
	public List<PrenotazioneDTO> getListaPrenotazioni();
}
