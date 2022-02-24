package it.gesev.mensa.service;

import java.util.List;

import it.gesev.mensa.dto.MensaDTO;

public interface MensaService 
{
	public List<MensaDTO> getAllMense();
	public int createMensa(MensaDTO mensaDTO);
	public int updateMensa(MensaDTO mensaDTO, int idMensa);
}
