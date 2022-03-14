package it.gesev.mensa.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface PrenotazioneService 
{
	public void insertPrenotazioni(MultipartFile file) throws IOException;
}
