package it.gesev.mensa.dao;

import java.util.List;

import it.gesev.mensa.entity.AssMensaTipoLocale;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.TipoFormaVettovagliamento;
import it.gesev.mensa.entity.TipoLocale;


public interface MensaDAO 
{
	public List<Mensa> getAllMense();
	public int createMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale, String descrizioneTipoVettovagliamento);
	public int updateMensa(Mensa mensa, List<AssMensaTipoLocale> assMensaTipoLocale, int idMensa, String descrizioneTipoVettovagliamento);
	public int disableMensa(Mensa mensa, int idMensa);
	public Mensa getSingolaMensa(int idMensa);
	
	public Mensa getFile(int idMensa);
	
	public List<TipoLocale> getAllLocali();
	
	public List<AssMensaTipoLocale> getAssMensaTipoLocaleByMensa(int idMensa);
	
	public List<Ente> getAllEnti();
	
	public List<TipoFormaVettovagliamento> getAllTipoFormaVettovagliamento();
	
}
