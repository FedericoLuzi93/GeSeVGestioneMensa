package it.gesev.mensa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Value("${gesev.italian.data.format}")
	private String italianDateFormat;
	
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
		Optional<Menu> menuDB = menuRepository.cercaMenuDelGiorno(dataMenu, optMensa.get().getCodiceMensa(), menu.getTipoDieta());
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
				Pietanza pietanza = null;
				if(pietanzaDTO.getIdPietanza() != null)
				{
					Optional<Pietanza> optPietanza = pietanzaRepository.findById(pietanzaDTO.getIdPietanza());
					if(!optPietanza.isPresent())
						throw new GesevException("ID pietanza non valido: " + pietanzaDTO.getIdPietanza(), HttpStatus.BAD_REQUEST);
					
					pietanza = optPietanza.get();
				}
				
				else
					pietanza = new Pietanza();
				
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

	@Override
	public Menu getMenuGiorno(Integer idMensa, Date dataMenu, Integer tipoDieta) 
	{
		logger.info("Ricerca menu del giorno...");
		
		Optional<Menu> optMenu = menuRepository.cercaMenuDelGiorno(dataMenu, idMensa, tipoDieta);
		if(!optMenu.isPresent())
			return null;
		
		else
			return optMenu.get();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDateConMenu(Integer idMensa, List<String> dateSettimana) 
	{
		logger.info("Controllo date della settimana...");
		
		if(idMensa == null)
			throw new GesevException("ID mensa non valido", HttpStatus.BAD_REQUEST);
		
		List<Object[]> results = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(dateSettimana))
		{
			
			String query = "select to_char(m.data_menu, 'YYYY-MM-DD') "
					+ "from menu m left join "
					+ "(select p.menu_fk, count(*) "
					+ "from pietanza p  "
					+ "group by p.menu_fk) cp  "
					+ "on m.id_menu = cp.menu_fk	 "
					+ "where m.mensa_fk = :idMensa and  "
					+ "to_char(m.data_menu, 'YYYY-MM-DD') in :dateSettimana";
			
			Query selectQuery = entityManager.createNativeQuery(query).setParameter("idMensa", idMensa).setParameter("dateSettimana", dateSettimana);
			results.addAll(selectQuery.getResultList());
		}
		
		return results;
	}

	@Override
	public void cancellaPietanza(Integer idPietanza) 
	{
		logger.info("Cancellazione pietanza...");
		
		if(idPietanza == null)
			throw new GesevException("ID pietanza non valido", HttpStatus.BAD_REQUEST);
		
		Optional<Pietanza> optPIetanza = pietanzaRepository.findById(idPietanza);
		if(!optPIetanza.isPresent())
			throw new GesevException("Nessuna pietanza trovata con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		pietanzaRepository.delete(optPIetanza.get());
		
	}
	
	

}
