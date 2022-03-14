package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import it.gesev.mensa.dto.ServizioEventoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.dto.TipoPastoDTO;
import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.ServizioEvento;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.entity.TipoPasto;


public interface MensaDAO 
{
	/* Mensa */
	public List<Mensa> getAllMense();
	public int createMensa(Mensa mensa, List<TipoLocaleDTO> listaTipoLocaleDTO, List<TipoPastoDTO> listaTipoPastoDTO,
			List<ServizioEventoDTO> listaServizioEventoDTO, int codiceTipoFormaVettovagliamento, int idEnte) throws ParseException;
	public int updateMensa(int idMensa, Mensa mensa, List<TipoLocaleDTO> listaTipoLocaleDTO,
			List<TipoPastoDTO> listaTipoPastoDTO, List<ServizioEventoDTO> listaServizioEventoDTO,
			int codiceTipoFormaVettovagliamento, int idEnte) throws ParseException;
	public int disableMensa(Mensa mensa, int idMensa);
	public Mensa getSingolaMensa(int idMensa);
	public List<Mensa> getMensaPerEnte(int idEnte);
	
	public Mensa getFile(int idMensa);
	
	/* Associative */
	public List<TipoLocale> getAllLocali();
	public List<TipoPasto> getAllTipoPasto();
	public List<TipoFormaVettovagliamento> getAllTipoFormaVettovagliamento();
	
	public List<Ente> getAllEnti();
	public List<Ente> getEntiFiltratiPerMensa(int idMensa);
	
	public List<ServizioEvento> getServizioEventoPerMensa(int idMensa);

	public List<AssMensaTipoLocale> getAssMensaTipoLocaleByMensa(int idMensa);
	public List<AssTipoPastoMensa> getServiziPerMensa(int idMensa);
	
	//Senza Service
	public Optional<TipoPasto> getTipoPastoPerId(int idTipoPasto);
	public List<TipoPasto> getTipoPastoPerLista(List<Integer> codiciPasto);



}
