package it.gesev.mensa.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dao.MensaDAO;
import it.gesev.mensa.dao.RuoliDAO;
import it.gesev.mensa.dto.AssDipendenteRuoloDTO;
import it.gesev.mensa.dto.DettaglioRuoloDTO;
import it.gesev.mensa.dto.DipendenteDTO;
import it.gesev.mensa.dto.OrganoDirettivoDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.dto.RuoloDTO;
import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.OrganoDirettivo;
import it.gesev.mensa.entity.RuoloMensa;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.utils.OrganoDirettivoMapper;

@Service
public class RuoliServiceImpl implements RuoliService 
{
	private static Logger logger = LoggerFactory.getLogger(RuoliServiceImpl.class);
	
	@Autowired
	private RuoliDAO ruoliDAO;
	
	@Autowired
	private MensaDAO mensaDao;
	
	@Override
	public DettaglioRuoloDTO getDettaglioRuoli() 
	{
		logger.info("Servizio per la ricerca dei dettagli dei ruoli...");
		DettaglioRuoloDTO dettaglio = new DettaglioRuoloDTO();
		
		List<Dipendente> listaDipendenti = ruoliDAO.getListaDipendenti();
		List<AssDipendenteRuolo> listaDipendentiRuoli = ruoliDAO.getListaDipendenteRuolo();
		
		ModelMapper mapper = new ModelMapper();
		
		/* conversione dei dipendenti */
		if(listaDipendenti.size() > 0)
		{
			List<DipendenteDTO> dipendentiDTO = new ArrayList<>();
			for(Dipendente dipendente : listaDipendenti)
				dipendentiDTO.add(mapper.map(dipendente, DipendenteDTO.class));
			
			dettaglio.setListaDipendenti(dipendentiDTO);
		}
		
		/* conversioni delle associazioni dei ruoli */
		if(listaDipendentiRuoli.size() > 0)
		{
			List<AssDipendenteRuoloDTO> listaRuoliDTO = new ArrayList<AssDipendenteRuoloDTO>();
			for(AssDipendenteRuolo ruolo : listaDipendentiRuoli)
			{
				AssDipendenteRuoloDTO ruoloDTO = new AssDipendenteRuoloDTO();
				ruoloDTO.setDipendente(mapper.map(ruolo.getDipendente(), DipendenteDTO.class));
				ruoloDTO.setRuolo(mapper.map(ruolo.getRuolo(), RuoloDTO.class));
				ruoloDTO.setAssDipendenteRuoloId(ruolo.getAssDipendenteRuoloId());
				ruoloDTO.setOrganoDirettivo(ruolo.getOrganoDirettivo() != null ? mapper.map(ruolo.getOrganoDirettivo(), OrganoDirettivoDTO.class) : null);
				
				listaRuoliDTO.add(ruoloDTO);
			}
			
			dettaglio.setAssociazioniRuolo(listaRuoliDTO);
		}
		
		return dettaglio;
		
	}

	@Override
	public List<OrganoDirettivoDTO> getListaOrganiDirettivi() 
	{
		logger.info("Servizio per la ricerca degli organi direttivi...");
		
		List<OrganoDirettivoDTO> listaOrganiDTO = new ArrayList<>();
		List<OrganoDirettivo> listaOrgani = ruoliDAO.getListaOrganiDirettivi();
		
		if(listaOrgani.size() >  0)
		{
			ModelMapper mapper = new ModelMapper();
			
			for(OrganoDirettivo organo : listaOrgani)
				listaOrganiDTO.add(mapper.map(organo, OrganoDirettivoDTO.class));
		}
		
		return listaOrganiDTO;
		
	}

	@Override
	public List<RuoloDTO> getRuoliByIdOrdineDirettivo() {
		logger.info("Servizio per la ricerca degli ruoli dall'ID dell'organo direttivo...");
		List<RuoloDTO> listaRuoliDTO = new ArrayList<>();
		List<RuoloMensa> listaRuoli = ruoliDAO.getRuoliByIdOrdineDirettivo();
		
		if(listaRuoli.size() > 0)
		{
			ModelMapper mapper = new ModelMapper();
			for(RuoloMensa ruolo : listaRuoli)
				listaRuoliDTO.add(mapper.map(ruolo, RuoloDTO.class));
		}
		
		return listaRuoliDTO;
	}

	@Override
	public void aggiungiRuoloDipendente(AssDipendenteRuoloDTO associazione) throws ParseException 
	{
		logger.info("Servizio per l'associazione del ruolo...");
		
		if(associazione.getIdMensa() == null)
			throw new GesevException("Impossibile trovare una mensa con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		ruoliDAO.aggiungiRuoloDipendente(associazione.getDipendente().getCodiceDipendente(), associazione.getRuolo().getCodiceRuoloMensa(), associazione.getOrganoDirettivo().getCodiceOrganoDirettivo());
		
	}

	@Override
	public DettaglioRuoloDTO ricercaDipendenti(List<RicercaColonnaDTO> listaColonne, Integer idMensa) {
		logger.info("Servizio per la ricerca dei dipendenti...");
		
		DettaglioRuoloDTO dettaglio = new DettaglioRuoloDTO();
		ModelMapper mapper = new ModelMapper();
		
		logger.info("Controllo esistenza mensa...");
		if(idMensa == null)
			throw new GesevException("ID mensa non valido", HttpStatus.BAD_REQUEST);
		
		Mensa mensa = mensaDao.getSingolaMensa(idMensa);
		if(mensa == null)
			throw new GesevException("Non e' stato possibile trovare una mensa con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Ricerca ente associato...");
		
		if(mensa.getEnte() == null || mensa.getEnte().getIdEnte() == null)
			throw new GesevException("Impossibile trovare l'ente associato alla mensa", HttpStatus.BAD_REQUEST);
		
		if(listaColonne != null && listaColonne.size() > 0)
		{
			List<Dipendente> listaDipendenti = ruoliDAO.ricercaDipendenti(listaColonne, mensa.getEnte().getIdEnte());
			logger.info("Trovati " + listaDipendenti.size() + " elementi.");
			
			List<DipendenteDTO> listaDTO = new ArrayList<>();
			for(Dipendente dipendente : listaDipendenti)
				listaDTO.add(mapper.map(dipendente, DipendenteDTO.class));
			
			dettaglio.setListaDipendenti(listaDTO);
		}
		
		/* lista ruoli */
//		List<AssDipendenteRuolo> listaDipendentiRuoli = ruoliDAO.getListaDipendenteRuolo();
		List<AssDipendenteRuolo> listaDipendentiRuoli = ruoliDAO.getListaDipendenteRuolo(mensa.getEnte().getIdEnte());
		if(listaDipendentiRuoli.size() > 0)
		{
			List<AssDipendenteRuoloDTO> listaRuoliDTO = new ArrayList<AssDipendenteRuoloDTO>();
			for(AssDipendenteRuolo ruolo : listaDipendentiRuoli)
			{
				AssDipendenteRuoloDTO ruoloDTO = new AssDipendenteRuoloDTO();
				ruoloDTO.setDipendente(mapper.map(ruolo.getDipendente(), DipendenteDTO.class));
				ruoloDTO.setRuolo(mapper.map(ruolo.getRuolo(), RuoloDTO.class));
				ruoloDTO.setAssDipendenteRuoloId(ruolo.getAssDipendenteRuoloId());
				ruoloDTO.setOrganoDirettivo(ruolo.getOrganoDirettivo() != null ? mapper.map(ruolo.getOrganoDirettivo(), OrganoDirettivoDTO.class) : null);
				
				listaRuoliDTO.add(ruoloDTO);
			}
			
			dettaglio.setAssociazioniRuolo(listaRuoliDTO);
		}
		
		return dettaglio;
	}

	@Override
	public DettaglioRuoloDTO updateRuoloDipendente(AssDipendenteRuoloDTO associazione) 
	{
		logger.info("Servizio per l'aggiornamento del ruolo del dipendente...");
		
		if(associazione == null || associazione.getAssDipendenteRuoloId() == null || associazione.getRuolo() == null || associazione.getDipendente() == null)
			throw new GesevException("I dati forniti non sono corretti", HttpStatus.BAD_REQUEST);
		
		if(associazione.getIdMensa() == null)
			throw new GesevException("Impossibile trovare una mensa con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		Mensa optMensa = mensaDao.getSingolaMensa(associazione.getIdMensa());
		if(optMensa == null)
			throw new GesevException("Impossibile trovare una mensa con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		ruoliDAO.updateRuoloDipendente(associazione.getAssDipendenteRuoloId(), associazione.getRuolo().getCodiceRuoloMensa(), 
				                       associazione.getDipendente().getCodiceDipendente(), 
				                       associazione.getOrganoDirettivo() != null ? associazione.getOrganoDirettivo().getCodiceOrganoDirettivo() : null);
		
		return findDipendenteByIdEnte(associazione.getIdMensa());
		
	}

	@Override
	public DettaglioRuoloDTO cancellaRuolo(Integer idRuoloDipendente, Integer idMensa) 
	{
		logger.info("Servizio per la cancellazione del ruolo del dipendente...");
		
		ruoliDAO.cancellaRuolo(idRuoloDipendente);
		
		return findDipendenteByIdEnte(idMensa); 
	}
	
	/* Crea Organo Direttivo */
	@Override
	public int creaNuovoOrganoDirettivo(OrganoDirettivoDTO organoDirettivoDTO) 
	{
		OrganoDirettivo organoDirettivo = null;
		try
		{
			logger.info("Servizio per la creazione del organo direttivo...");
			organoDirettivo = OrganoDirettivoMapper.mapToEntity(organoDirettivoDTO);
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio creaNuovoOrganoDirettivo", exc);
			throw new GesevException("Non è stato possibile inserire il nuovo organo direttivo", HttpStatus.BAD_REQUEST);
		}
		logger.info("Creazione nuovo organo direttivo corso...");
		ruoliDAO.creaOrganoDirettivo(organoDirettivo);
		return organoDirettivo.getCodiceOrganoDirettivo();
	}

	/* Modifica Organo Direttivo */
	@Override
	public int modificaOrganoDirettivo(OrganoDirettivoDTO organoDirettivoDTO, int idOrganoDirettivo) 
	{
		OrganoDirettivo organoDirettivo = null;
		try
		{
			logger.info("Servizio per la modifica organo direttivo...");
			organoDirettivo = OrganoDirettivoMapper.mapToEntity(organoDirettivoDTO);
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio modificaOrganoDirettivo", exc);
			throw new GesevException("Non è stato possibile modificare organo direttivo", HttpStatus.BAD_REQUEST);
		}
		logger.info("Modifica organo direttivo corso...");
		ruoliDAO.modificaOrganoDirettivo(organoDirettivo, idOrganoDirettivo);
		return idOrganoDirettivo;
	}

	/* Cancella Organo Direttivo */
	@Override
	public int cancellaOrganoDirettivo(int idOrganoDirettivo) 
	{
		logger.info("Servizio per la cancellazione di un organo direttivo...");
		ruoliDAO.cancellaOrganoDirettivo(idOrganoDirettivo);
		return idOrganoDirettivo;
	}
	
	@Override
	public DettaglioRuoloDTO findDipendenteByIdEnte(Integer idMensa) 
	{
		logger.info("Servizio per la ricerca dei dipendenti della mensa...");
		
		if(idMensa == null)
			throw new GesevException("Impossibile trovare una mensa con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		List<Dipendente> listaDipendenti = ruoliDAO.findDipendenteByIdEnte(idMensa);
		
		List<AssDipendenteRuolo> listaAss = listaDipendenti.size() > 0 ? ruoliDAO.getListaDipendenteRuolo(listaDipendenti.get(0).getEnte().getIdEnte()) : null;
		
		List<DipendenteDTO> listaDTO = new ArrayList<>();
		List<AssDipendenteRuoloDTO> listaAssDTO = new ArrayList<>();
		DettaglioRuoloDTO dettaglio = new DettaglioRuoloDTO();
		
		ModelMapper mapper = new ModelMapper();
		
		if(listaDipendenti.size() > 0)
		{
			for(Dipendente dipendente : listaDipendenti)
				listaDTO.add(mapper.map(dipendente, DipendenteDTO.class));
		}
		
		if(listaAss.size() > 0)
		{
			for(AssDipendenteRuolo ass : listaAss)
				listaAssDTO.add(mapper.map(ass, AssDipendenteRuoloDTO.class));
		}
		
		dettaglio.setAssociazioniRuolo(listaAssDTO);
		dettaglio.setListaDipendenti(listaDTO);
		
		return dettaglio;
	}

}
