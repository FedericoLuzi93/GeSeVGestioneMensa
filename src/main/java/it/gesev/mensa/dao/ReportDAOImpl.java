package it.gesev.mensa.dao;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import it.gesev.mensa.dto.DC4TabellaAllegatoCDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.ForzaEffettiva;
import it.gesev.mensa.entity.IdentificativoSistema;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.PastiConsumati;
import it.gesev.mensa.entity.TipoPagamento;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.DipendenteRepository;
import it.gesev.mensa.repository.ForzaEffettivaRepository;
import it.gesev.mensa.repository.IdentificativoSistemaRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.PastiConsumatiRepository;
import it.gesev.mensa.repository.TipoPagamentoRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.utils.ControlloData;

@Repository
@Component
public class ReportDAOImpl implements ReportDAO
{
	private static Logger logger = LoggerFactory.getLogger(ReportDAOImpl.class);

	@Value("${gesev.data.format}")
	private String dateFormat;

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

	@PersistenceContext
	EntityManager entityManager;

	/* Carica pasti consumati CSV*/
	@Override
	public void caricaPastiConsumati(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException 
	{
		logger.info("Accesso a caricaPastiConsumati classe ReportDAOImpl");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

		for(PastiConsumatiDTO pcCSV : listaPastiConsumatiCSV)
		{
			PastiConsumati pc = new PastiConsumati();

			logger.info("Controllo e assegnazione campi in corso...");
			Optional<Mensa> optionalMensa = mensaRepository.findByCodiceMensa(pcCSV.getMensa());
			if(!optionalMensa.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, mensa non valida", HttpStatus.BAD_REQUEST);
			pc.setMensa(optionalMensa.get());

			pc.setDataPasto(simpleDateFormat.parse(pcCSV.getDataPasto()));

			pc.setNome(pcCSV.getNome());
			pc.setCognome(pcCSV.getCognome());
			pc.setCodiceFiscale(pcCSV.getCodiceFiscale());

			if(StringUtils.isBlank(pcCSV.getCmd()) && !StringUtils.isBlank(pcCSV.getTipoPersonale()))
			{
				Optional<Dipendente> optionalDipendente = dipendenteRepository.findByCodiceFiscale(pcCSV.getCodiceFiscale());
				pc.setCmd(optionalDipendente.get().getCmd());	
			}

			if(!StringUtils.isBlank(pcCSV.getTipoPersonale()))
				pc.setTipoPersonale(pcCSV.getTipoPersonale());

			Optional<TipoPagamento> optionalPagamento = tipoPagamentoRepository.findByIdTipoPagamento(pcCSV.getTipoPagamento());
			if(!optionalPagamento.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, tipo pagamento non valido", HttpStatus.BAD_REQUEST);
			pc.setTipoPagamento(optionalPagamento.get());

			Optional<TipoPasto> optionalTipoPasto = tipoPastoRepository.findById(pcCSV.getTipoPasto());
			if(!optionalTipoPasto.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, tipo pasto non valido", HttpStatus.BAD_REQUEST);
			pc.setTipoPasto(optionalTipoPasto.get());

			pc.setOraIngresso(ControlloData.controlloTempo(pcCSV.getOraIngresso()));

			pastiConsumatiRepository.save(pc);
		}
	}

	/* Richiesta documento DC4 */
	@Override
	public List<DC4TabellaDTO> richiestaDocumentoDC4(DC4RichiestaDTO dc4RichiestaDTO, boolean includiPrenotati, boolean includiConsumati,
			boolean includiForzaEffettiva) throws ParseException 
	{
		logger.info("Accesso a richiestaDocumentoDC4 classe ReportDAOImpl");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Map<String, DC4TabellaDTO> map = new HashMap<String, DC4TabellaDTO>();
		String dataPerMappa = "";

		//Ordinati
		String anno = "'" + dc4RichiestaDTO.getAnno();
		String giorno = anno.concat("-" + dc4RichiestaDTO.getMese() + "-%'");

		int enteFk = dc4RichiestaDTO.getIdEnte();
		int idOperatore = dc4RichiestaDTO.getIdOperatore();

		if(StringUtils.isBlank(giorno))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		if(includiPrenotati)
		{
			String queryOrdinati = "select\r\n"
					+ "prenotati.data_prenotazione,\r\n"
					+ "prenotati.COLAZIONE,\r\n"
					+ "prenotati.PRANZO,\r\n"
					+ "prenotati.CENA,\r\n"
					+ "case when fqd.id_firma is not null then 'Y' else 'N' end FIRMATO\r\n"
					+ "from\r\n"
					+ "(select p.data_prenotazione,\r\n"
					+ "sum(case when p.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE,\r\n"
					+ "sum(case when p.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO,\r\n"
					+ "sum(case when p.tipo_pasto_fk = 3 then 1 else 0 end) as CENA\r\n"
					+ "from prenotazione p\r\n"
					+ "left join mensa m on p.identificativo_mensa_fk = m.codice_mensa \r\n"
					+ "where to_char(p.data_prenotazione, 'YYYY-MM-DD') like " + giorno + " \r\n"
					+ "and m.ente_fk = " + enteFk + " \r\n"
					+ "group by p.data_prenotazione\r\n"
					+ "order by p.data_prenotazione) prenotati\r\n"
					+ "left join firma_quotidiana_dc4 fqd on prenotati.data_prenotazione = fqd.data_firma and fqd.id_operatore = " + idOperatore + " \r\n"
					+ "order by prenotati.data_prenotazione;";

			logger.info("Esecuzione query: " + queryOrdinati); 
			Query ordQuery = entityManager.createNativeQuery(queryOrdinati);
			List<Object[]> listOfResultsOrdinati = ordQuery.getResultList();

			for(Object[] obj : listOfResultsOrdinati)
			{
				DC4TabellaDTO dc4 = new DC4TabellaDTO();

				Integer colazioneOrdinati = ((BigInteger) obj[1]).intValue();
				dc4.setColazioneOrdinati(colazioneOrdinati);
				Integer pranzoOrdinati = ((BigInteger) obj[2]).intValue();
				dc4.setPranzoOrdinati(pranzoOrdinati);
				Integer cenaOrdinati = ((BigInteger) obj[3]).intValue();
				dc4.setCenaOridnati(cenaOrdinati);

				String firma = (String) obj[4];
				if(firma.equalsIgnoreCase("Y"))
					dc4.setFirma("SI");
				else
					dc4.setFirma("NO");

				Date dataReport = ((Date) obj[0]);
				dc4.setGiorno(simpleDateFormat.format(dataReport));
				dataPerMappa = dc4.getGiorno();

				map.put((String) simpleDateFormat.format(dataReport),dc4); 
			}
		}

		if(includiConsumati)
		{
			//Consumati
			String queryConsumati = "select	sum(case when p.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE,\r\n"
					+ "		sum(case when p.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO,\r\n"
					+ "		sum(case when p.tipo_pasto_fk = 3 then 1 else 0 end) as CENA,\r\n"
					+ "  	p.data_pasto \r\n"
					+ "from pasti_consumati p \r\n"
					+ "left join mensa m on p.mensa_fk = m.codice_mensa \r\n"
					+ "where	m.ente_fk = " + enteFk + " \r\n"
					+ "group by p.data_pasto \r\n"
					+ "order by p.data_pasto;";

			logger.info("Esecuzione query: " + queryConsumati); 
			Query consQuery = entityManager.createNativeQuery(queryConsumati);
			List<Object[]> listOfResultsConsumati = consQuery.getResultList();

			for(Object[] obj : listOfResultsConsumati)
			{
				Integer colazioneConsumati = ((BigInteger) obj[0]).intValue();
				Integer pranzoConsumati = ((BigInteger) obj[1]).intValue();
				Integer cenaConsumati = ((BigInteger) obj[2]).intValue();

				Date dataReport = (Date) obj[3];
				DC4TabellaDTO dto = map.get(simpleDateFormat.format(dataReport));
				boolean isDtoNull = dto == null;
				if(isDtoNull)
					dto = new DC4TabellaDTO();

				dto.setColazioneConsumati(colazioneConsumati);
				dto.setPranzoConsumati(pranzoConsumati);
				dto.setCenaConsumati(cenaConsumati);

				if(isDtoNull)
					map.put(simpleDateFormat.format(dataReport), dto);
			}
		}

		//Effettivi
		logger.info("Query forza effettiva in corso...");
		if(includiForzaEffettiva)
		{
			Date dataInizio = simpleDateFormat.parse(dc4RichiestaDTO.getAnno() + "-" + dc4RichiestaDTO.getMese() + "-01");

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataInizio);
			int maxGiorno = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, maxGiorno);

			List<ForzaEffettiva> listaForzaEff = forzaEffettivaRepository.listaForzaEffettiva(dc4RichiestaDTO.getIdEnte(), dataInizio, calendar.getTime());

			if(listaForzaEff.size() > 0)
			{
				ForzaEffettiva fel0 = listaForzaEff.get(0);
				String descrizioneEnte = fel0.getEnte().getDescrizioneEnte();

				for(ForzaEffettiva fe : listaForzaEff)
				{
					DC4TabellaDTO dto = map.get(simpleDateFormat.format(fe.getDataRiferimento()));
					boolean isDtoNull = dto == null;
					if(isDtoNull)
						dto = new DC4TabellaDTO();

					dto.setColazioneEffettiva(fe.getNumDipendenti());
					dto.setPranzoEffettiva(fe.getNumDipendenti());
					dto.setCenaEffettiva(fe.getNumDipendenti());

					dto.setDescrizioneEnte(descrizioneEnte);

					if(isDtoNull)
						map.put(simpleDateFormat.format(simpleDateFormat.format(fe.getDataRiferimento())), dto);
				}
			}
		}

		List<DC4TabellaDTO> listaDC4TabellaDTO = map.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());

		logger.info("Lista DC4 creata con successo");
		return listaDC4TabellaDTO;
	}

	/* Richiesta Firme DC4 */
	@Override
	public List<FirmeDC4> richiestaFirmeDC4(DC4RichiestaDTO dc4RichiestaDTO)
	{
		logger.info("Accesso a richiestaFirmeDC4 classe ReportDAOImpl");

		int enteFk = dc4RichiestaDTO.getIdEnte();

		String queryFirme = "select arrm.ordine_firma, rm.descrizione_ruolo_mensa, d.nome, d.cognome, arrm.ruolo_fk\r\n"
				+ "from ass_report_ruolo_mensa arrm\r\n"
				+ "left join ruolo_mensa rm on arrm.ruolo_fk  = rm.codice_ruolo_mensa\r\n"
				+ "left join ass_dipendente_ruolo adr on rm.codice_ruolo_mensa = adr.ruolo_fk\r\n"
				+ "left join dipendente d on adr.dipendente_fk = d.codice_dipendente\r\n"
				+ "left join mensa m on adr.mensa_fk = m.codice_mensa \r\n"
				+ "where arrm.report_fk = 'DC4D' and d.codice_dipendente is not null\r\n"
				+ "and m.ente_fk = "+ enteFk + " \r\n"
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

	/* Richiesta documento DC4 Allegato C */
	@Override
	public List<DC4TabellaAllegatoCDTO> richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO) 
	{
		logger.info("Accesso a richiestaDocumentoDC4AllegatoC classe ReportDAOImpl");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Map<String, DC4TabellaAllegatoCDTO> map = new HashMap<String, DC4TabellaAllegatoCDTO>();

		//Pasti UFC	
		String giornoGraduati = "'%";
		String dataTotale = giornoGraduati.concat("-" + dc4RichiestaDTO.getMese() + "-" + dc4RichiestaDTO.getAnno() + "'");

		if(StringUtils.isBlank(dataTotale))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		String queryUFC = "select\r\n"
				+ "pc.data_pasto,\r\n"
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO,\r\n"
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA\r\n"
				+ "from pasti_consumati pc left join dipendente d\r\n"
				+ "on pc.codice_fiscale = d.codice_fiscale\r\n"
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk\r\n"
				+ "where pc.tipo_pagamento_fk = 'TG' and (d.codice_dipendente is null or g.tipo_grado_fk in ('UF', 'SU') or d.tipo_personale = 'C')\r\n"
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + "\r\n"
				+ "group by pc.data_pasto\r\n"
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryUFC); 
		Query numUFCQuery = entityManager.createNativeQuery(queryUFC);
		List<Object[]> listOfResultsPastiUFC = numUFCQuery.getResultList();

		for(Object[] obj : listOfResultsPastiUFC)
		{
			DC4TabellaAllegatoCDTO dc4AllC = new DC4TabellaAllegatoCDTO();

			Integer numpranziUSC = ((BigInteger) obj[1]).intValue();
			dc4AllC.setNumpranziUSC(numpranziUSC);
			Integer numCeneUSC = ((BigInteger) obj[2]).intValue();
			dc4AllC.setNumCeneUSC(numCeneUSC);

			Date dataReport = ((Date) obj[0]);
			dc4AllC.setGiorno(simpleDateFormat.format(dataReport));

			map.put((String) simpleDateFormat.format(dataReport),dc4AllC); 
		}

		//Pasti Graduati
		String queryGraduati = "select\r\n"
				+ "pc.data_pasto,\r\n"
				+ "sum(case when pc.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE,\r\n"
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO,\r\n"
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA\r\n"
				+ "from pasti_consumati pc left join dipendente d\r\n"
				+ "on pc.codice_fiscale = d.codice_fiscale\r\n"
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk\r\n"
				+ "where pc.tipo_pagamento_fk = 'TG' and g.tipo_grado_fk = 'GT'\r\n"
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + "\r\n"
				+ "group by pc.data_pasto\r\n"
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryGraduati); 
		Query graduatiQuery = entityManager.createNativeQuery(queryGraduati);
		List<Object[]> listOfResultsPastiGraduati = graduatiQuery.getResultList();

		for(Object[] obj : listOfResultsPastiGraduati)
		{
			Integer numColazioniGraduati = ((BigInteger) obj[1]).intValue();
			Integer numPranziGraduati = ((BigInteger) obj[2]).intValue();
			Integer numCeneGraduati = ((BigInteger) obj[3]).intValue();

			Date dataReport = (Date) obj[0];
			DC4TabellaAllegatoCDTO dto = map.get(simpleDateFormat.format(dataReport));
			boolean isDtoNull = dto == null;
			if(isDtoNull)
				dto = new DC4TabellaAllegatoCDTO();

			dto.setNumColazioniGraduati(numColazioniGraduati);
			dto.setNumPranziGraduati(numPranziGraduati);
			dto.setNumCeneGraduati(numCeneGraduati);

			if(isDtoNull)
				map.put(simpleDateFormat.format(dataReport), dto);
		}

		List<DC4TabellaAllegatoCDTO> listaDC4TabellaAllegatoCDTO = map.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());

		logger.info("Lista creata con successo");
		return listaDC4TabellaAllegatoCDTO;
	}

	/* Leggi tutti identificativi Sistema */
	@Override
	public List<IdentificativoSistema> getAllIdentificativiSistema() 
	{
		logger.info("Accesso a getAllIdentificativiSistema classe ReportDAOImpl");
		return identificativoSistemaRepository.findAll();
	}



}
