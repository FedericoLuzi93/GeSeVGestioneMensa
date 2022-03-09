package it.gesev.mensa.dao;

import java.util.List;
import java.util.Optional;

import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;
import it.gesev.mensa.entity.TipoPasto;


public interface MensaDAO 
{
	/* Mensa */
	public List<Mensa> getAllMense();
	public int createMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale, List<AssTipoPastoMensa> assTipoPastoMensa, String descrizioneTipoVettovagliamento);
	public int updateMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale, int idMensa, String descrizioneTipoVettovagliamento);
	public int disableMensa(Mensa mensa, int idMensa);
	public Mensa getSingolaMensa(int idMensa);
	public List<Mensa> getMensaPerEnte(int idEnte);
	
	public Mensa getFile(int idMensa);
	
	/* Associative */
	public List<TipoLocale> getAllLocali();
	
	public List<AssMensaTipoLocale> getAssMensaTipoLocaleByMensa(int idMensa);
	
	public List<Ente> getAllEnti();
	
	public List<TipoFormaVettovagliamento> getAllTipoFormaVettovagliamento();
	public List<TipoPasto> getAllTipoPasto();

	public List<AssTipoPastoMensa> getServiziPerMensa(int idMensa);
	
	//Senza Service
	public Optional<TipoPasto> getTipoPastoPerId(int idTipoPasto);
}
