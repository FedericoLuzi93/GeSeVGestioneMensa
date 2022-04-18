package it.gesev.mensa.service;

import java.util.Date;

import it.gesev.mensa.dto.MenuDTO;

public interface MenuService 
{
	public void inserisciMenu(MenuDTO menu);
	public MenuDTO getMenuGiorno(MenuDTO menuDTO);
	
}
