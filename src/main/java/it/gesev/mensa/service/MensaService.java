package it.gesev.mensa.service;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;

public interface MensaService 
{
	public List<MensaDTO> getAllMense();
	public int createMensa(CreaMensaDTO creaMensaDTO) throws ParseException;
	public int updateMensa(CreaMensaDTO creaMensaDTO, int idMensa) throws ParseException;
	public int disableMensa(MensaDTO mensaDTO, int idMensa) throws ParseException;
	
	public List<TipoLocaleDTO> getAllLocali();
	
	public List<EnteDTO> getAllEnti();
}
