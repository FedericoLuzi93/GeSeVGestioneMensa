package it.gesev.mensa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.dto.PietanzaDTO;
import it.gesev.mensa.entity.Menu;
import it.gesev.mensa.entity.Pietanza;
import it.gesev.mensa.exc.GesevException;

@Service
public class MenuServiceImpl implements MenuService
{
	@Autowired
	private MenuDAO menuDAO;
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	@Override
	public void inserisciMenu(MenuDTO menu) 
	{
		logger.info("Servizio per aggiunta menu...");
		
		menuDAO.inserisciMenu(menu);
		
	}

	@Override
	public MenuDTO getMenuGiorno(MenuDTO menuDTO) 
	{
		logger.info("Servizio per ricerca menu del giorno...");
		
		if(menuDTO.getIdMensa() == null || StringUtils.isBlank(menuDTO.getDataMenu()))
			throw new GesevException("Dati dell'ID mensa o della data menu non validi", HttpStatus.BAD_REQUEST);
		
		SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
		Date dataMenu = null;
		
		try
		{
			dataMenu = formatter.parse(menuDTO.getDataMenu());
		}
		
		catch(Exception ex)
		{
			throw new GesevException("Formato data menu non valido", HttpStatus.BAD_REQUEST);
		}
		
		
		MenuDTO menu = new MenuDTO();
		Menu fonudMenu = menuDAO.getMenuGiorno(menuDTO.getIdMensa(), dataMenu);
		
		menu.setListaPietanze(new ArrayList<>());
		menu.setDataMenu(formatter.format(fonudMenu.getDataMenu()));
		menu.setIdMensa(fonudMenu.getMensa().getCodiceMensa());
		menu.setIdMenu(fonudMenu.getIdMenu());
		menu.setTipoDieta(fonudMenu.getTipoDieta().getIdTipoDieta());
		
		for(Pietanza pietanza : fonudMenu.getListaPietanze())
		{
			PietanzaDTO dto = new PietanzaDTO();
			dto.setDescrizionePietanza(pietanza.getDescrizionePietanza());
			dto.setIdPietanza(pietanza.getIdPietanza());
			dto.setTipoPasto(pietanza.getTipoPasto().getCodiceTipoPasto());
			dto.setTipoPietanza(pietanza.getTipoPietanza().getIdTipoPietanza());
			
			menu.getListaPietanze().add(dto);
		}
		
		return menu;
	}

	

}
