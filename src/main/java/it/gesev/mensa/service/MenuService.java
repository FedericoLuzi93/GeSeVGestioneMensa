package it.gesev.mensa.service;

import java.util.Date;
import java.util.List;

import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.dto.PairDTO;

public interface MenuService 
{
	public void inserisciMenu(MenuDTO menu);
	public MenuDTO getMenuGiorno(MenuDTO menuDTO);
	public List<PairDTO<String, String>> getDateConMenu(MenuDTO menu);
	
}
