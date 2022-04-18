package it.gesev.mensa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.dto.PietanzaDTO;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.Menu;
import it.gesev.mensa.entity.Pietanza;
import it.gesev.mensa.entity.TipoDieta;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.entity.TipoPietanza;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.MenuRepository;
import it.gesev.mensa.repository.PietanzaRepository;
import it.gesev.mensa.repository.TipoDietaRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.repository.TipoPietanzaRepository;

@Component
public class MenuDAOImpl implements MenuDAO 
{
	@Autowired
	private MensaRepository mensaRepository;
	
	@Autowired
	private TipoDietaRepository tipoDietaRepository;
	
	@Autowired
	private PietanzaRepository pietanzaRepository;
	
	@Autowired
	private TipoPietanzaRepository tipoPietanzaRepository;
	
	@Autowired
	private TipoPastoRepository tipoPastoRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	private static Logger logger = LoggerFactory.getLogger(MenuDAOImpl.class);
	
	@Override
	@Transactional
	public void inserisciMenu(MenuDTO menu) 
	{
		logger.info("Inserimento menu...");
		
		/* inserimento della data */
		Date dataMenu = null;
		if(StringUtils.isBlank(menu.getDataMenu()))
			throw new GesevException("Data menu non valida", HttpStatus.BAD_REQUEST);
		
		SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
		try
		{
			dataMenu = formatter.parse(menu.getDataMenu());
		}
		
		catch(Exception ex)
		{
			throw new GesevException("Data menu non valida", HttpStatus.BAD_REQUEST); 
		}
		
		/* ricerca mensa */
		if(menu.getIdMensa() == null)
			throw new GesevException("Identificativo mensa non valido", HttpStatus.BAD_REQUEST);
		
		Optional<Mensa> optMensa = mensaRepository.findById(menu.getIdMensa());
		if(!optMensa.isPresent())
			throw new GesevException("Impossibile trovare una mensa con l'ID " + menu.getIdMensa(), HttpStatus.BAD_REQUEST);
		
		/* ricerca menu */
		Menu menuSalvato = null;
		Optional<Menu> menuDB = menuRepository.cercaMenuDelGiorno(dataMenu, optMensa.get().getCodiceMensa());
		if(menuDB.isPresent())
			menuSalvato = menuDB.get();
		
		else
		{
			Menu menuEntity = new Menu();
			menuEntity.setDataMenu(dataMenu);
			menuEntity.setMensa(optMensa.get());
			
			/* controllo tipo dieta */
			if(menu.getTipoDieta() == null)
				throw new GesevException("Tipo dieta non valido", HttpStatus.BAD_REQUEST);
			
			Optional<TipoDieta> optTipoDieta = tipoDietaRepository.findById(menu.getTipoDieta());
			if(!optTipoDieta.isPresent())
				throw new GesevException("Tipo dieta non valido", HttpStatus.BAD_REQUEST);
			
			menuEntity.setTipoDieta(optTipoDieta.get());
			
			menuSalvato = menuRepository.save(menuEntity);
		}
		
		
		List<Pietanza> listaPietanze = new ArrayList<>();
		
		/* inserimento pietanze */
		if(!CollectionUtils.isEmpty(menu.getListaPietanze()))
		{
			logger.info("Controllo pietanze...");
			for(PietanzaDTO pietanzaDTO : menu.getListaPietanze())
			{
				/* controllo tipo pietanza */
				if(pietanzaDTO.getTipoPietanza() == null)
					throw new GesevException("Tipo pietanza non valido per una o piu' pietanze", HttpStatus.BAD_REQUEST);
				
				Optional<TipoPietanza> optTipoPietanza = tipoPietanzaRepository.findById(pietanzaDTO.getTipoPietanza());
				if(!optTipoPietanza.isPresent())
					throw new GesevException("Tipo pietanza non valido per una o piu' pietanze", HttpStatus.BAD_REQUEST);
				
				/* controllo tipo pasto */
				if(pietanzaDTO.getTipoPasto() == null)
					throw new GesevException("Tipo pasto non valido per una o piu' pietanze", HttpStatus.BAD_REQUEST);
				
				Optional<TipoPasto> optTipoPasto = tipoPastoRepository.findById(pietanzaDTO.getTipoPasto());
				if(!optTipoPasto.isPresent())
					throw new GesevException("Tipo pasto non valido per una o piu' pietanze", HttpStatus.BAD_REQUEST);
				
				if(StringUtils.isBlank(pietanzaDTO.getDescrizionePietanza()))
					throw new GesevException("Descrizione tipo pietanza non valido per una o piu' pietanze", HttpStatus.BAD_REQUEST);
				
				Pietanza pietanza = new Pietanza();
				pietanza.setDescrizionePietanza(pietanzaDTO.getDescrizionePietanza());
				pietanza.setMenu(menuSalvato);
				pietanza.setTipoPietanza(optTipoPietanza.get());
				pietanza.setTipoPasto(optTipoPasto.get());
				
				listaPietanze.add(pietanzaRepository.save(pietanza));
			}
		}
		
		if(!CollectionUtils.isEmpty(listaPietanze))
		{
			if(CollectionUtils.isEmpty(menuSalvato.getListaPietanze()))
				menuSalvato.setListaPietanze(listaPietanze);
			
			else
				menuSalvato.getListaPietanze().addAll(listaPietanze);
			
			menuRepository.save(menuSalvato);
		}
		
		logger.info("Fine inserimento menu");
		
		
	}

}
