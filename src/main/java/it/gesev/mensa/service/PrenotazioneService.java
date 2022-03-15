package it.gesev.mensa.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dto.PrenotazioneDTO;

public interface PrenotazioneService 
{
	public void insertPrenotazioni(MultipartFile file) throws IOException;
	public List<PrenotazioneDTO> getListaPrenotazioni();
}
