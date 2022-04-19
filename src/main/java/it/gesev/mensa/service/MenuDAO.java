package it.gesev.mensa.service;

import java.util.Date;
import java.util.List;

import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.entity.Menu;

public interface MenuDAO 
{
	public void inserisciMenu(MenuDTO menu);
	public Menu getMenuGiorno(Integer idMensa, Date dataMenu, Integer tipoDieta);
	public List<Object[]> getDateConMenu(Integer idMensa, List<String> dateSettimana);
	public void cancellaPietanza(Integer idPietanza);
}
