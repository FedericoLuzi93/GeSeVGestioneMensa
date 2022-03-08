package it.gesev.mensa.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.dto.FELocaliDTO;
import it.gesev.mensa.dto.FileDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.dto.TipoFromaVettovagliamentoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;

public interface MensaService 
{
	public List<MensaDTO> getAllMense();
	public int createMensa(CreaMensaDTO creaMensaDTO, MultipartFile multipartFile) throws ParseException, IOException;
	public int updateMensa(CreaMensaDTO creaMensaDTO, int idMensa, MultipartFile multipartFile) throws ParseException, IOException;
	public int disableMensa(MensaDTO mensaDTO, int idMensa) throws ParseException;
	public MensaDTO getSingolaMensa(int idMensa);
	public List<MensaDTO> getMensaPerEnte(int idEnte);
	
	public FileDTO getFile(int idMensa);
	
	public List<TipoLocaleDTO> getAllLocali();
	public List<FELocaliDTO> getLocaliPerMensa(int idMensa);
	
	public List<EnteDTO> getAllEnti();
	
	public List<TipoFromaVettovagliamentoDTO> getAllTipoFormaVettovagliamento();	
}
