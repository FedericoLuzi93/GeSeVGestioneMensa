package it.gesev.mensa.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import it.gesev.mensa.entity.Grado;
import it.gesev.mensa.entity.IdentificativoSistema;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.Prenotazione;
import it.gesev.mensa.entity.StrutturaOrganizzativa;
import it.gesev.mensa.entity.TipoDieta;
import it.gesev.mensa.entity.TipoGrado;
import it.gesev.mensa.entity.TipoPagamento;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.entity.TipoRazione;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.GradoRepositoory;
import it.gesev.mensa.repository.IdentificativoSistemaRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.PrenotazioneRepository;
import it.gesev.mensa.repository.StrutturaOrganizzativaReposittory;
import it.gesev.mensa.repository.TipoDietaRepository;
import it.gesev.mensa.repository.TipoGradoRepository;
import it.gesev.mensa.repository.TipoPagamentoRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.repository.TipoRazioneRepository;

@Component
public class PrenotazioneDAOImpl implements PrenotazioneDAO 
{
	private static Logger logger = LoggerFactory.getLogger(PrenotazioneDAOImpl.class);
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Value("${gesev.batch.size}")
	private Integer batchSize;
	
	@Autowired
	private IdentificativoSistemaRepository identificativoRepository;
	
	@Autowired
	private TipoPastoRepository tipoPastoRepository;
	
	@Autowired
	private TipoDietaRepository tipoDietaRepository;
	
	@Autowired
	private TipoRazioneRepository tipoRazioneRepository;
	
	@Autowired
	private PrenotazioneRepository prenotazioneRepository;
	
	@Autowired
	private MensaRepository mensaRepository;
	
	@Autowired
	private GradoRepositoory gradoRepository;
	
	@Autowired
	private TipoGradoRepository tipoGradoRepository;
	
	@Autowired
	private TipoPagamentoRepository tipoPagamentoRepository;
	
	@Autowired
	private StrutturaOrganizzativaReposittory strutturaOrganizzativaRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	@Transactional
	public void insertPrenotazione(List<CaricamentoPrenotazioniDTO> listaPrenotazioni) 
	{
		logger.info("Inserimento prenotazioni...");
		
		if(listaPrenotazioni == null || listaPrenotazioni.size() == 0)
			return;
		
		SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
		int rowCounter = 0;
		
		Map<String, IdentificativoSistema> mappaIdSistema = new HashMap<>();
		Map<Integer, Mensa> mappaMense = new HashMap<>();
		Map<String, Grado> mappaGrado = new HashMap<>();
		Map<String, TipoGrado> mappaTipoGrado = new HashMap<>();
		Map<String, StrutturaOrganizzativa> mappaStrutturaOrganizzativa = new HashMap<>();
		Map<String, TipoPagamento> mappaTipoPagamento = new HashMap<>();
		Map<Integer, TipoPasto> mappaTipoPasto = new HashMap<>();
		Map<Integer, TipoDieta> mappaTipoDieta = new HashMap<>();
		Map<String, TipoRazione> mappaTipoRazione = new HashMap<>();
				
		for(CaricamentoPrenotazioniDTO prenotazione : listaPrenotazioni)
		{
			if(rowCounter % this.batchSize == 0)
			{
				entityManager.flush();
				entityManager.clear();
			}
			
			logger.info("Analisi riga " + (++rowCounter));
			
			/* identificativo sistema */
			if(StringUtils.isBlank(prenotazione.getIdentificativoSistema()))
				throw new GesevException("Dati del sistema identificativo non validi", HttpStatus.BAD_REQUEST);
			
			IdentificativoSistema sistema = mappaIdSistema.get(prenotazione.getIdentificativoSistema());
			if(sistema == null)
			{
				Optional<IdentificativoSistema> optIdentificativo = identificativoRepository.findById(prenotazione.getIdentificativoSistema());
				if(!optIdentificativo.isPresent())
					throw new GesevException("Nessun identificativo di sistema trovato con l'ID " + prenotazione.getIdentificativoSistema(), HttpStatus.BAD_REQUEST);
				
				sistema = optIdentificativo.get();
				mappaIdSistema.put(prenotazione.getIdentificativoSistema(), optIdentificativo.get());
			}
			
			/* mensa */
			if(StringUtils.isBlank(prenotazione.getMensa()) || !prenotazione.getMensa().matches("^[0-9]+$"))
				throw new GesevException("Dati della mensa non validi", HttpStatus.BAD_REQUEST);
			
			Mensa mensa = mappaMense.get(Integer.valueOf(prenotazione.getMensa()));
			if(mensa == null)
			{
				Optional<Mensa> optMensa = mensaRepository.findById(Integer.valueOf(prenotazione.getMensa()));
				if(!optMensa.isPresent())
						throw new GesevException("Dati della mensa non validi", HttpStatus.BAD_REQUEST);
				
				mensa = optMensa.get();
				mappaMense.put(Integer.valueOf(prenotazione.getMensa()), mensa);
			}
			
			/* data prenotazione */
			if(StringUtils.isBlank(prenotazione.getDataPrenotazione()))
				throw new GesevException("La data di prenotazione non e' corretta", HttpStatus.BAD_REQUEST);
			
			Date dataPrenotazione = null;
			try
			{
				dataPrenotazione = formatter.parse(prenotazione.getDataPrenotazione());
			}
			
			catch(Exception ex)
			{
				throw new GesevException("Il valore della data di prenotazione " + prenotazione.getDataPrenotazione() + " non e' valido", HttpStatus.BAD_REQUEST);
			}
			
			/* codice fiscale */
			if(StringUtils.isBlank(prenotazione.getCodiceFiscale()) || prenotazione.getCodiceFiscale().length() != 16)
				throw new GesevException("Codice fiscale non valido", HttpStatus.BAD_REQUEST);
			
			/* nome e cognome */
			if(StringUtils.isBlank(prenotazione.getNome()) || StringUtils.isBlank(prenotazione.getCognome()))
				throw new GesevException("Dati nome/cognome non validi", HttpStatus.BAD_REQUEST);
			
//			if(StringUtils.isBlank(prenotazione.getTipoPersonale()))
//				throw new GesevException("Dati nome/cognome non validi", HttpStatus.BAD_REQUEST);
			
			/* grado */
			Grado grado = null;
			if(StringUtils.isNotBlank(prenotazione.getGrado()))
			{
				grado = mappaGrado.get(prenotazione.getGrado());
				if(grado == null)
				{
					Optional<Grado> optGrado = gradoRepository.findById(prenotazione.getGrado());
					if(!optGrado.isPresent())
						throw new GesevException("Dati grado non validi", HttpStatus.BAD_REQUEST);
					
					grado = optGrado.get();
					mappaGrado.put(prenotazione.getGrado(), grado);
				}
			}
		
			/* tipo grado */
			TipoGrado tipoGrado = null;
			if(StringUtils.isNotBlank(prenotazione.getTipoGrado()))
			{
				tipoGrado = mappaTipoGrado.get(prenotazione.getTipoGrado());
				if(tipoGrado == null)
				{
					Optional<TipoGrado> optTipoGrado = tipoGradoRepository.findById(prenotazione.getTipoGrado());
					if(!optTipoGrado.isPresent())
						throw new GesevException("Dati tipo grado non validi", HttpStatus.BAD_REQUEST);
					
					tipoGrado = optTipoGrado.get();
					mappaTipoGrado.put(prenotazione.getTipoGrado(), tipoGrado);
				}
			}
			
			/* struttura organizzativa */
			StrutturaOrganizzativa strutturaOrganizzattiva = null;
			if(StringUtils.isNotBlank(prenotazione.getStrutturaOrganizzativa()))
			{
				strutturaOrganizzattiva = mappaStrutturaOrganizzativa.get(prenotazione.getStrutturaOrganizzativa());
				if(strutturaOrganizzattiva == null)
				{
					Optional<StrutturaOrganizzativa> optStrutturaOrganizzativa = strutturaOrganizzativaRepository.findById(prenotazione.getStrutturaOrganizzativa());
					if(!optStrutturaOrganizzativa.isPresent())
						throw new GesevException("Dati struttura organizzativa non validi", HttpStatus.BAD_REQUEST);
					
					strutturaOrganizzattiva = optStrutturaOrganizzativa.get();
					mappaStrutturaOrganizzativa.put(prenotazione.getStrutturaOrganizzativa(), strutturaOrganizzattiva);
				}
			}
			
			
//			if(StringUtils.isBlank(prenotazione.getDenominazioneUnitaFunzionale()))
//				throw new GesevException("Dati deenominazione unita' funzionali non validi", HttpStatus.BAD_REQUEST);
			
			/* commensale esterno */
			if(StringUtils.isBlank(prenotazione.getCommensaleEsterno()))
				throw new GesevException("Dati commensale non validi", HttpStatus.BAD_REQUEST);
			
			/* tipo pagamento */
			if(StringUtils.isAllBlank(prenotazione.getTipoPagamento()))
				throw new GesevException("Dati tipo pagamento non validi", HttpStatus.BAD_REQUEST);
			
			TipoPagamento tipoPagamento = mappaTipoPagamento.get(prenotazione.getTipoPagamento());
			if(tipoPagamento == null)
			{
				Optional<TipoPagamento> optTipoPagamento = tipoPagamentoRepository.findById(prenotazione.getTipoPagamento());
				if(!optTipoPagamento.isPresent())
					throw new GesevException("Dati tipo pagamento non validi", HttpStatus.BAD_REQUEST);
				
				tipoPagamento = optTipoPagamento.get();
				mappaTipoPagamento.put(prenotazione.getTipoPagamento(), tipoPagamento);
			}
			
			/* tipo pasto */
			if(StringUtils.isBlank(prenotazione.getTipoPasto()) || !prenotazione.getTipoPasto().matches("^[0-9]+$"))
				throw new GesevException("I dati del tipo pasto non sono validi", HttpStatus.BAD_REQUEST);
			
			TipoPasto tipoPasto = mappaTipoPasto.get(Integer.valueOf(prenotazione.getTipoPasto()));
			if(tipoPasto == null)
			{
				Optional<TipoPasto> optTipoPasto = tipoPastoRepository.findById(Integer.valueOf(prenotazione.getTipoPasto()));
				if(!optTipoPasto.isPresent())
					throw new GesevException("Nessun tipo pasto trovato con ID " + prenotazione.getTipoPasto(), HttpStatus.BAD_REQUEST);
				
				tipoPasto = optTipoPasto.get();
				mappaTipoPasto.put(Integer.valueOf(prenotazione.getTipoPasto()), tipoPasto);
			}
			
			/* flag cestino */
			if(StringUtils.isBlank(prenotazione.getFlagCestino()) || prenotazione.getFlagCestino().length() != 1 || 
					  (!prenotazione.getFlagCestino().equalsIgnoreCase("Y") && !prenotazione.getFlagCestino().equalsIgnoreCase("N")))
						throw new GesevException("Flag cestino non valido", HttpStatus.BAD_REQUEST);
			
			/* tipo dieta */
			if(StringUtils.isBlank(prenotazione.getTipoDieta()) || !prenotazione.getTipoDieta().matches("^[0-9]+$"))
				throw new GesevException("I dati del tipo dieta non sono validi", HttpStatus.BAD_REQUEST);
			
			TipoDieta tipoDieta = mappaTipoDieta.get(Integer.valueOf(prenotazione.getTipoDieta()));
			if(tipoDieta == null)
			{
				Optional<TipoDieta> optTipoDieta = tipoDietaRepository.findById(Integer.valueOf(prenotazione.getTipoDieta()));
				if(!optTipoDieta.isPresent())
					throw new GesevException("Nessun tipo pasto trovato con l'ID " + prenotazione.getTipoDieta(), HttpStatus.BAD_REQUEST);
				
				tipoDieta = optTipoDieta.get();
				mappaTipoDieta.put(Integer.valueOf(prenotazione.getTipoDieta()), tipoDieta);
			}
			
			/* tipo razione */
			if(StringUtils.isBlank(prenotazione.getTipoRazione()))
				throw new GesevException("Nessun tipo razione trovato con l'ID " + prenotazione.getTipoRazione(), HttpStatus.BAD_REQUEST);
			
			TipoRazione tipoRazione = mappaTipoRazione.get(prenotazione.getTipoRazione());
			if(tipoRazione == null)
			{
				Optional<TipoRazione> optTipoRazione = tipoRazioneRepository.findByIdTipoRazione(prenotazione.getTipoRazione());
				if(!optTipoRazione.isPresent())
					throw new GesevException("Nessun tipo razione trovato con l'ID " + prenotazione.getTipoRazione(), HttpStatus.BAD_REQUEST);
				
				tipoRazione = optTipoRazione.get();
				mappaTipoRazione.put(prenotazione.getTipoRazione(), tipoRazione);
			}
			
				
			Prenotazione nuovaPrenotazione = new Prenotazione();
			nuovaPrenotazione.setIdentificativoSistema(sistema);
			nuovaPrenotazione.setMensa(mensa);
			nuovaPrenotazione.setDataPrenotazione(dataPrenotazione);
			nuovaPrenotazione.setCodiceFiscale(prenotazione.getCodiceFiscale());
			nuovaPrenotazione.setNome(prenotazione.getNome());
			nuovaPrenotazione.setCognome(prenotazione.getCognome());
			nuovaPrenotazione.setTipoPersonale(StringUtils.isNotBlank(prenotazione.getTipoPersonale()) ? prenotazione.getTipoPersonale() : null);
			nuovaPrenotazione.setGrado(grado);
			nuovaPrenotazione.setTipoGrado(tipoGrado);
			nuovaPrenotazione.setStrutturaOrganizzativa(strutturaOrganizzattiva);
			nuovaPrenotazione.setDenominazioneUnitaFunzionale(StringUtils.isNotBlank(prenotazione.getDenominazioneUnitaFunzionale()) ? prenotazione.getDenominazioneUnitaFunzionale() : null);
			nuovaPrenotazione.setCommensaleEsterno(prenotazione.getCommensaleEsterno());
			nuovaPrenotazione.setTipoPagamento(tipoPagamento);
			nuovaPrenotazione.setTipoPasto(tipoPasto);
			nuovaPrenotazione.setFlagCestino(prenotazione.getFlagCestino());
			nuovaPrenotazione.setTipoDieta(tipoDieta);
			nuovaPrenotazione.setTipoRazione(tipoRazione);
			
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
