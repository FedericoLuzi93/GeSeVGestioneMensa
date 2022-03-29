package it.gesev.mensa.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.mensa.dto.CaricamentoPrenotazioniDTO;
import it.gesev.mensa.dto.PrenotazioneDTO;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.IdentificativoSistema;
import it.gesev.mensa.entity.Prenotazione;
import it.gesev.mensa.entity.TipoDieta;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.entity.TipoRazione;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.IdentificativoSistemaRepository;
import it.gesev.mensa.repository.PrenotazioneRepository;
import it.gesev.mensa.repository.TipoDietaRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.repository.TipoRazioneRepository;

@Component
public class PrenotazioneDAOImpl implements PrenotazioneDAO 
{
	private static Logger logger = LoggerFactory.getLogger(PrenotazioneDAOImpl.class);
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Autowired
	private IdentificativoSistemaRepository identificativoRepository;
	
	@Autowired
	private EnteRepository enteRepository;
	
	@Autowired
	private TipoPastoRepository tipoPastoRepository;
	
	@Autowired
	private TipoDietaRepository tipoDietaRepository;
	
	@Autowired
	private TipoRazioneRepository tipoRazioneRepository;
	
	@Autowired
	private PrenotazioneRepository prenotazioneRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	@Transactional
	public void insertPrenotazione(List<CaricamentoPrenotazioniDTO> listaPrenotazioni) 
	{
		logger.info("Inserimento prenotazioni...");
		
		for(CaricamentoPrenotazioniDTO prenotazione : listaPrenotazioni)
		{
			logger.info("Controllo identificativo...");
			if(prenotazione.getIdentificativoSistema() == null || prenotazione.getIdentificativoSistema().getIdSistema() == null)
				throw new GesevException("Dati del sistema identificativo non validi", HttpStatus.BAD_REQUEST);
			
			Optional<IdentificativoSistema> optIdenetificativo = identificativoRepository.findById(prenotazione.getIdentificativoSistema().getIdSistema());
			if(!optIdenetificativo.isPresent())
				throw new GesevException("Nessun identificativo di sistema trovato con l'ID " + prenotazione.getIdentificativoSistema().getIdSistema(), HttpStatus.BAD_REQUEST);
			
			logger.info("Controllo ente...");
			if(prenotazione.getEnte() == null || prenotazione.getEnte().getIdEnte() == null)
				throw new GesevException("I dati dell'ente non sono validi", HttpStatus.BAD_REQUEST);
			
			Optional<Ente> optEnte = enteRepository.findById(prenotazione.getEnte().getIdEnte());
			if(!optEnte.isPresent())
				throw new GesevException("Nessuna ente trovata con ID " + prenotazione.getEnte().getIdEnte(), HttpStatus.BAD_REQUEST);
			
			logger.info("Controllo tipo pasto...");
			if(prenotazione.getTipoPasto() == null || prenotazione.getTipoPasto().getCodiceTipoPasto() == null)
				throw new GesevException("I dati del tipo pasto non sono validi", HttpStatus.BAD_REQUEST);
			
			Optional<TipoPasto> optTipoPasto = tipoPastoRepository.findById(prenotazione.getTipoPasto().getCodiceTipoPasto());
			if(!optTipoPasto.isPresent())
				throw new GesevException("Nessun tipo pasto trovato con ID " + prenotazione.getTipoPasto().getCodiceTipoPasto(), HttpStatus.BAD_REQUEST);
			
			logger.info("Controllo tipo dieta...");
			if(prenotazione.getTipoDieta() == null || prenotazione.getTipoDieta().getIdTipoDieta() == null)
				throw new GesevException("I dati del tipo dieta non sono validi", HttpStatus.BAD_REQUEST);
			
			Optional<TipoDieta> optTipoDieta = tipoDietaRepository.findById(prenotazione.getTipoDieta().getIdTipoDieta());
			if(!optTipoDieta.isPresent())
				throw new GesevException("Nessun tipo pasto trovato con l'ID " + prenotazione.getTipoDieta().getIdTipoDieta(), HttpStatus.BAD_REQUEST);
			
			logger.info("Controllo tipo razione...");
			if(prenotazione.getTipoRazione() == null || prenotazione.getTipoRazione().getIdTipoRazione() == null)
				throw new GesevException("Nessun tipo razione trovato con l'ID " + prenotazione.getTipoRazione().getIdTipoRazione(), HttpStatus.BAD_REQUEST);
			
			Optional<TipoRazione> optTipoRazione = tipoRazioneRepository.findByIdTipoRazione(prenotazione.getTipoRazione().getIdTipoRazione());
			if(!optTipoRazione.isPresent())
				throw new GesevException("Nessun tipo razione trovato con l'ID " + prenotazione.getTipoRazione().getIdTipoRazione(), HttpStatus.BAD_REQUEST);
			
			logger.info("Controllo data prenotazione...");
			if(StringUtils.isBlank(prenotazione.getDataPrenotazione()))
				throw new GesevException("La data di prenotazione non e' corretta", HttpStatus.BAD_REQUEST);
				
			Date dataPrenotazione = null;
			SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
			try
			{
				dataPrenotazione = formatter.parse(prenotazione.getDataPrenotazione());
			}
			
			catch(Exception ex)
			{
				throw new GesevException("Il valore della data di prenotazione " + prenotazione.getDataPrenotazione() + " non e' valido", HttpStatus.BAD_REQUEST);
			}
			
			if(StringUtils.isBlank(prenotazione.getCodiceFiscale()) || prenotazione.getCodiceFiscale().length() != 16)
				throw new GesevException("Codice fiscale non valido", HttpStatus.BAD_REQUEST);
			
			if(StringUtils.isBlank(prenotazione.getFlagCestino()) || prenotazione.getFlagCestino().length() != 1 || 
			  (!prenotazione.getFlagCestino().equalsIgnoreCase("Y") && !prenotazione.getFlagCestino().equalsIgnoreCase("N")))
				throw new GesevException("Flag cestino non valido", HttpStatus.BAD_REQUEST);
			
			logger.info("Creazione preenotazione...");
			Prenotazione nuovaPrenotazione = new Prenotazione();
			nuovaPrenotazione.setIdentificativoSistema(optIdenetificativo.get());
			nuovaPrenotazione.setEnte(optEnte.get());
			nuovaPrenotazione.setTipoPasto(optTipoPasto.get());
			nuovaPrenotazione.setTipoDieta(optTipoDieta.get());
			nuovaPrenotazione.setTipoRazione(optTipoRazione.get());
			nuovaPrenotazione.setDataPrenotazione(dataPrenotazione);
			nuovaPrenotazione.setCodiceFiscale(prenotazione.getCodiceFiscale());
			nuovaPrenotazione.setFlagCestino(prenotazione.getFlagCestino());
			
			prenotazioneRepository.save(nuovaPrenotazione);
		}
		
		logger.info("Fine salvataggio prenotazioni");

	}

	@Override
	public List<PrenotazioneDTO> getListaPrenotazioni() 
	{
		logger.info("Ricerca lista prenotazioni...");
		
		List<PrenotazioneDTO> listaPrenotazioni = new ArrayList<>();
		String query = "select sistema.descrizione_sistema, e.descrizione_ente, TO_CHAR(p.data_prenotazione, 'DD-MM-YYYY'), p.codice_fiscale, "
				+ "d.nome || ' ' || d.cognome as NOME_COGNOME, "
				+ "case when d.tipo_personale = 'M' then 'Militare' else 'Civile' end as TIPO_PERSONALE, "
				+ "d.grado, tp.descrizione as tipo_pasto, "
				+ "case when p.flag_cestino = 'Y' then 'SI' else 'NO' end FLAG_CESTINO, "
				+ "td.descrizione_tipo_dieta, "
				+ "tr.descrizione_tipo_razione "
				+ "from prenotazione p "
				+ "left join identificativo_sistema sistema on p.identificativo_sistema_fk = sistema.id_sistema "
				+ "left join ente e on p.ente_fk = e.id_ente "
				+ "left join dipendente d on p.codice_fiscale = d.codice_fiscale "
				+ "left join tipo_pasto tp on tp.codice_tipo_pasto = p.tipo_pasto_fk "
				+ "left join tipo_dieta td on p.tipo_dieta_fk = td.id_tipo_dieta "
				+ "left join tipo_razione tr on tr.id_tipo_razione = p.tipo_razione_fk ";
		
		Query selectQuery = entityManager.createNativeQuery(query);
		@SuppressWarnings("unchecked")
		List<Object[]> results = selectQuery.getResultList();
		
		for(Object[] record : results)
		{
			PrenotazioneDTO prenotazione = new PrenotazioneDTO();
			prenotazione.setSistemaPersonale((String)record[0]);
			prenotazione.setDenominazioneEnte((String)record[1]);
			prenotazione.setDataPrenotazione((String)record[2]);
			prenotazione.setCodiceFiscale((String)record[3]);
			prenotazione.setNomeCognome((String)record[4]);
			prenotazione.setTipoPersonale((String)record[5]);
			prenotazione.setGrado((String)record[6]);
			prenotazione.setTipoPasto((String)record[7]);
			prenotazione.setFlagCestino((String)record[8]);
			prenotazione.setTipoDieta((String)record[9]);
			prenotazione.setTipoRazione((String)record[10]);
			
			listaPrenotazioni.add(prenotazione);
		}
		
		return listaPrenotazioni;
	}

}
