package it.gesev.mensa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.dto.PairDTO;
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
		
		if(menuDTO.getIdMensa() == null || StringUtils.isBlank(menuDTO.getDataMenu()) || menuDTO.getTipoDieta() == null)
			throw new GesevException("Dati dell'ID mensa, della data menu o del tipo dieta non validi", HttpStatus.BAD_REQUEST);
		
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
		Menu fonudMenu = menuDAO.getMenuGiorno(menuDTO.getIdMensa(), dataMenu, menuDTO.getTipoDieta());
		if(fonudMenu == null)
			return null;
		
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

	@Override
	public List<PairDTO<String, String>> getDateConMenu(MenuDTO menu) 
	{
		logger.info("Servizio controllo date settimanali del menu...");
		List<PairDTO<String, String>> listaDate = new ArrayList<>();
		
		List<Object[]> listaRecord = menuDAO.getDateConMenu(menu.getIdMensa(), menu.getDateSettimana());
		if(!CollectionUtils.isEmpty(listaRecord))
		{
			Set<String> dateTemporanee = new HashSet<>(menu.getDateSettimana());
			
			for(int counter = 0; counter < listaRecord.size(); counter++)
			{
				Object item = listaRecord.get(counter);
				
				PairDTO<String, String> dataFlag = new PairDTO<>();
				dataFlag.setChiave((String)(item));
				dataFlag.setValore("Y");
				dateTemporanee.remove(dataFlag.getChiave());
				
				listaDate.add(dataFlag);
			}
			
			for(String chiave : dateTemporanee)
			{
				PairDTO<String, String> dataFlag = new PairDTO<>();
				dataFlag.setChiave(chiave);
				dataFlag.setValore("N");
				
				listaDate.add(dataFlag);
			}
			
		}
		
		return listaDate;
	}

	@Override
	public void cancellaPietanza(Integer idPietanza) {
		logger.info("Servizio per la cancellazione delle pietanze...");
		
		menuDAO.cancellaPietanza(idPietanza);
		
	}

	

}
