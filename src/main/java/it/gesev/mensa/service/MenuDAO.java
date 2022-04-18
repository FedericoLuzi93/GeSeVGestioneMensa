package it.gesev.mensa.service;

import java.util.Date;

import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.entity.Menu;

public interface MenuDAO 
{
	public void inserisciMenu(MenuDTO menu);
	public Menu getMenuGiorno(Integer idMensa, Date dataMenu);
}
