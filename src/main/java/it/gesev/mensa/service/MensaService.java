package it.gesev.mensa.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.dto.FELocaliDTO;
import it.gesev.mensa.dto.FEMensaCompletaDTO;
import it.gesev.mensa.dto.FEServizioMensaDTO;
import it.gesev.mensa.dto.FileDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.dto.ServizioEventoDTO;
import it.gesev.mensa.dto.TipoDietaDTO;
import it.gesev.mensa.dto.TipoFromaVettovagliamentoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.dto.TipoPastoDTO;

public interface MensaService 
{
	/* Mensa */
	public List<MensaDTO> getAllMense();
	public int createMensa(CreaMensaDTO creaMensaDTO, MultipartFile multipartFile) throws ParseException, IOException;
	public int updateMensa(CreaMensaDTO creaMensaDTO, int idMensa, MultipartFile multipartFile) throws ParseException, IOException;
	public int disableMensa(MensaDTO mensaDTO, int idMensa) throws ParseException;
	public MensaDTO getSingolaMensa(int idMensa);
	public FEMensaCompletaDTO getSingolaMensaCompleta(int idMensa);
	public List<MensaDTO> getMensaPerEnte(int idEnte);
	
	public FileDTO getFile(int idMensa);
	
	/* Associative */
	public List<TipoLocaleDTO> getAllLocali();
	public List<FELocaliDTO> getLocaliPerMensa(int idMensa);
	
	public List<EnteDTO> getAllEnti();
	public List<EnteDTO> getEntiFiltratiPerMensa(int idMensa);	
	
	public List<TipoFromaVettovagliamentoDTO> getAllTipoFormaVettovagliamento();
	public List<TipoPastoDTO> getAllTipoPasto();
	
	public FEServizioMensaDTO getServiziPerMensa(int idMensa);
	public List<ServizioEventoDTO> getServizioEventoPerMensa(int idMensa);
	
	public List<TipoDietaDTO> getAllTipoDieta();
	public List<TipoDietaDTO> getTipoDietaPerMensa(int idMensa);

	public List<MensaDTO> ricercaMense(int idEnte, List<RicercaColonnaDTO> colonne);


}
