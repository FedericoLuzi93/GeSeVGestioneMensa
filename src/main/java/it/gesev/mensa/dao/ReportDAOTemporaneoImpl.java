package it.gesev.mensa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.jasper.ReportGeSeV3Jasper;
import it.gesev.mensa.repository.DipendenteRepository;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.FirmaQuodidianaRepository;
import it.gesev.mensa.repository.ForzaEffettivaRepository;
import it.gesev.mensa.repository.IdentificativoSistemaRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.PastiConsumatiRepository;
import it.gesev.mensa.repository.PietanzaRepository;
import it.gesev.mensa.repository.PrenotazioneRepository;
import it.gesev.mensa.repository.TipoPagamentoRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.repository.TipoRazioneRepository;

@Repository
@Component
public class ReportDAOTemporaneoImpl implements ReportDAOTemporaneo 
{
	private static Logger logger = LoggerFactory.getLogger(ReportDAOTemporaneo.class);

	@Value("${gesev.data.format}")
	private String dateFormat;

	@Value("${gesev.italian.data.format}")
	private String italianDateFormat;

	@Autowired
	private MensaRepository mensaRepository;

	@Autowired
	private TipoPastoRepository tipoPastoRepository;

	@Autowired
	private TipoPagamentoRepository tipoPagamentoRepository;

	@Autowired
	private DipendenteRepository dipendenteRepository;

	@Autowired
	private PastiConsumatiRepository pastiConsumatiRepository;

	@Autowired
	private ForzaEffettivaRepository forzaEffettivaRepository;

	@Autowired
	private IdentificativoSistemaRepository identificativoSistemaRepository;

	@Autowired
	private FirmaQuodidianaRepository firmaQuodidianaRepository;

	@Autowired
	private EnteRepository enteRepository;

	@Autowired
	private PietanzaRepository pietanzaRepository;

	@Autowired
	private TipoRazioneRepository tipoRazioneRepository;
	
	@Autowired
	private PrenotazioneRepository prenotazioneRepository;

	@PersistenceContext
	EntityManager entityManager;

	/* Download File Report Gesev 3 */
	@Override
	public List<ReportGeSeV3Jasper> downloadReportGesev3(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException 
	{
		logger.info("Accesso a downloadReportGesev3 classe ReportDAOImpl");

		//Controllo Ente
		Optional<Ente> optionalEnte = enteRepository.findById(dc4RichiestaDTO.getIdEnte());

		if(!optionalEnte.isPresent())
			throw new GesevException("Non è stato possibile creare il report, ente non presente");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

		//String dataYYYYmmDD = "'" + dc4RichiestaDTO.getAnno().concat("-" + dc4RichiestaDTO.getMese().concat("-" + dc4RichiestaDTO.getGiorno()) + "'");
		String dataCorretta = dc4RichiestaDTO.getAnno().concat("-" + dc4RichiestaDTO.getMese().concat("-" + dc4RichiestaDTO.getGiorno()));
		Date giornoDatato = simpleDateFormat.parse(dataCorretta);

		if(StringUtils.isBlank(dataCorretta))
			throw new GesevException("Impossibile generare il documento DC1 Nominativo, data non valida", HttpStatus.BAD_REQUEST);
		
		
		
		
		
		return null;
	}

	/* Lista Firme Gesev 3 */
	@Override
	public List<FirmeDC4> listaFirmeReportGesev3(DC4RichiestaDTO dc4RichiestaDTO) 
	{
		logger.info("Accesso a richiestaFirmeDC1 classe ReportDAOImpl");

		int enteFk = dc4RichiestaDTO.getIdEnte();

		String queryFirme = "select arrm.ordine_firma, rm.descrizione_ruolo_mensa, d.nome, d.cognome, arrm.ruolo_fk "
				+ "from ass_report_ruolo_mensa arrm "
				+ "left join ruolo_mensa rm on arrm.ruolo_fk  = rm.codice_ruolo_mensa "
				+ "left join ass_dipendente_ruolo adr on rm.codice_ruolo_mensa = adr.ruolo_fk "
				+ "left join dipendente d on adr.dipendente_fk = d.codice_dipendente "
				+ "left join mensa m on adr.mensa_fk = m.codice_mensa  "
				+ "where arrm.report_fk = 'DC1' and d.codice_dipendente is not null "
				+ "and m.ente_fk = "+ enteFk + "  "
				+ "order by arrm.ordine_firma;";

		logger.info("Esecuzione query: " + queryFirme); 
		Query consQuery = entityManager.createNativeQuery(queryFirme);
		List<Object[]> listOfResultsFirme = consQuery.getResultList();

		List<FirmeDC4> listaFirmeDC4 = new ArrayList<>();
		for(Object[] obj : listOfResultsFirme)
		{
			FirmeDC4 firmaDC4 = new FirmeDC4();
			firmaDC4.setDescrizione((String) obj[1]);
			firmaDC4.setNome((String) obj[2]);
			firmaDC4.setCognome((String) obj[3]);

			listaFirmeDC4.add(firmaDC4);
		}

		logger.info("Lista firme creata con successo");
		return listaFirmeDC4;
	}

}
