package it.gesev.mensa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dto.MenuDTO;

@Service
public class MenuServiceImpl implements MenuService
{
	@Autowired
	private MenuDAO menuDAO;
	
	private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	@Override
	public void inserisciMenu(MenuDTO menu) 
	{
		logger.info("Servizio per aggiunta menu...");
		
		menuDAO.inserisciMenu(menu);
		
	}

	

}
