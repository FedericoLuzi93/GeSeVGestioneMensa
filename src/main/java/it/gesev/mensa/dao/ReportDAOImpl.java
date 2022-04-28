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
import it.gesev.mensa.dto.FEPastiDC4Graduati;
import it.gesev.mensa.dto.FEPastiDC4USC;
import it.gesev.mensa.dto.FirmaQuotidianaDC4DTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.dto.MenuLeggeroDTO;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.dto.SendListPastiDC4AllegatoC;
import it.gesev.mensa.dto.SendListaDC1Prenotati;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.Ente;
import it.gesev.mensa.entity.FirmaQuodidiana;
import it.gesev.mensa.entity.ForzaEffettiva;
import it.gesev.mensa.entity.IdentificativoSistema;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.PastiConsumati;
import it.gesev.mensa.entity.Pietanza;
import it.gesev.mensa.entity.TipoPagamento;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.entity.TipoRazione;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.jasper.DC1NomJasper;
import it.gesev.mensa.jasper.DC1NomNumericaJasper;
import it.gesev.mensa.repository.DipendenteRepository;
import it.gesev.mensa.repository.EnteRepository;
import it.gesev.mensa.repository.FirmaQuodidianaRepository;
import it.gesev.mensa.repository.ForzaEffettivaRepository;
import it.gesev.mensa.repository.IdentificativoSistemaRepository;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.PastiConsumatiRepository;
import it.gesev.mensa.repository.PietanzaRepository;
import it.gesev.mensa.repository.TipoPagamentoRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.repository.TipoRazioneRepository;
import it.gesev.mensa.utils.ControlloData;

@Repository
@Component
public class ReportDAOImpl implements ReportDAO
{
	private static Logger logger = LoggerFactory.getLogger(ReportDAOImpl.class);

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
			
			Optional<IdentificativoSistema> optionalIdSistema = identificativoSistemaRepository.findById(pcCSV.getIdentificativoSistema());
			if(!optionalIdSistema.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, identificativo sistema non valido", HttpStatus.BAD_REQUEST);
			pc.setIdentificativoSistema(optionalIdSistema.get());
			
			Optional<TipoRazione> optionalTipoRazione = tipoRazioneRepository.findByIdTipoRazione(pcCSV.getTipoRazione());
			if(!optionalTipoRazione.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, tipo razione non valido", HttpStatus.BAD_REQUEST);
			pc.setTipoRazione(optionalTipoRazione.get());

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

		//Prenotati
		String anno = "'" + dc4RichiestaDTO.getAnno();
		String giorno = anno.concat("-" + dc4RichiestaDTO.getMese() + "-%'");

		int enteFk = dc4RichiestaDTO.getIdEnte();
		int idOperatore = dc4RichiestaDTO.getIdOperatore();

		if(StringUtils.isBlank(giorno))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		if(includiPrenotati)
		{
			String queryOrdinati = "select "
					+ "prenotati.data_prenotazione, "
					+ "prenotati.COLAZIONE, "
					+ "prenotati.PRANZO, "
					+ "prenotati.CENA "
					//					+ "case when fqd.id_firma is not null then 'Y' else 'N' end FIRMATO "
					+ "from "
					+ "(select p.data_prenotazione, "
					+ "sum(case when p.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE, "
					+ "sum(case when p.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
					+ "sum(case when p.tipo_pasto_fk = 3 then 1 else 0 end) as CENA "
					+ "from prenotazione p "
					+ "left join mensa m on p.identificativo_mensa_fk = m.codice_mensa  "
					+ "where to_char(p.data_prenotazione, 'YYYY-MM-DD') like " + giorno + "  "
					+ "and m.ente_fk = " + enteFk + " ";

			if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
				queryOrdinati = queryOrdinati + "and p.identificativo_sistema_fk = :idPersonale ";

			queryOrdinati = queryOrdinati 	+ "group by p.data_prenotazione "
					+ "order by p.data_prenotazione) prenotati "
					//											+ "left join firma_quotidiana_dc4 fqd on prenotati.data_prenotazione = fqd.data_firma and fqd.id_operatore = " + idOperatore + "  "
					+ "order by prenotati.data_prenotazione;";

			logger.info("Esecuzione query: " + queryOrdinati); 
			Query ordQuery = entityManager.createNativeQuery(queryOrdinati);

			if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
				ordQuery = ordQuery.setParameter("idPersonale", dc4RichiestaDTO.getSistemaPersonale());

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

				//				String firma = (String) obj[4];
				//				if(firma.equalsIgnoreCase("Y"))
				//					dc4.setFirma("SI");
				//				else
				//					dc4.setFirma("NO");

				Date dataReport = ((Date) obj[0]);
				dc4.setGiorno(simpleDateFormat.format(dataReport));
				dataPerMappa = dc4.getGiorno();

				map.put((String) simpleDateFormat.format(dataReport),dc4); 
			}
		}

		if(includiConsumati)
		{
			//Consumati
			String queryConsumati = "select	sum(case when p.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE, "
					+ "		sum(case when p.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
					+ "		sum(case when p.tipo_pasto_fk = 3 then 1 else 0 end) as CENA, "
					+ "  	p.data_pasto  "
					+ "from pasti_consumati p  "
					+ "left join mensa m on p.mensa_fk = m.codice_mensa  "
					+ "where	m.ente_fk = " + enteFk + "  "
					+ "and to_char(p.data_pasto, 'YYYY-MM-DD') like " + giorno + "  ";

			if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
				queryConsumati = queryConsumati + "and p.identificativo_sistema_fk = :idPersonale ";

			queryConsumati = queryConsumati	+ "group by p.data_pasto  "
					+ "order by p.data_pasto;";

			logger.info("Esecuzione query: " + queryConsumati); 
			Query consQuery = entityManager.createNativeQuery(queryConsumati);

			if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
				consQuery = consQuery.setParameter("idPersonale", dc4RichiestaDTO.getSistemaPersonale());

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

					dto.setGiorno(simpleDateFormat.format(fe.getDataRiferimento()));

					dto.setDescrizioneEnte(descrizioneEnte);

					if(isDtoNull)
						map.put(simpleDateFormat.format(fe.getDataRiferimento()), dto);
				}
			}
		}

		//Firme
		logger.info("Ricerca firme del mese in corso...");
		Date dataInizio = simpleDateFormat.parse(dc4RichiestaDTO.getAnno() + "-" + dc4RichiestaDTO.getMese() + "-01");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataInizio);
		int maxGiorno = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, maxGiorno);

		List<Date> listaDate = firmaQuodidianaRepository.getDateFirmeMensili(dataInizio, calendar.getTime());

		for(Date dataFirmata :  listaDate)
		{
			String dataFormattata = simpleDateFormat.format(dataFirmata);
			if(map.containsKey(dataFormattata))
				map.get(dataFormattata).setFirma("SI");	
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

		String queryFirme = "select arrm.ordine_firma, rm.descrizione_ruolo_mensa, d.nome, d.cognome, arrm.ruolo_fk "
				+ "from ass_report_ruolo_mensa arrm "
				+ "left join ruolo_mensa rm on arrm.ruolo_fk  = rm.codice_ruolo_mensa "
				+ "left join ass_dipendente_ruolo adr on rm.codice_ruolo_mensa = adr.ruolo_fk "
				+ "left join dipendente d on adr.dipendente_fk = d.codice_dipendente "
				+ "left join mensa m on adr.mensa_fk = m.codice_mensa  "
				+ "where arrm.report_fk = 'DC4C' and d.codice_dipendente is not null "
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

	/* Richiesta documento DC4 Allegato C */
	@Override
	public SendListPastiDC4AllegatoC richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO, 
			List<FEPastiDC4USC> listaPastiUFC, List<FEPastiDC4Graduati> listaPastiGraduati, SendListPastiDC4AllegatoC sendObjList)
	{
		logger.info("Accesso a richiestaDocumentoDC4AllegatoC classe ReportDAOImpl");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(italianDateFormat);

		//Pasti UFC	
		String giornoGraduati = "'%";
		String dataTotale = giornoGraduati.concat("-" + dc4RichiestaDTO.getMese() + "-" + dc4RichiestaDTO.getAnno() + "'");

		if(StringUtils.isBlank(dataTotale))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		String queryUFC = "select "
				+ "pc.data_pasto, "
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA "
				+ "from pasti_consumati pc left join dipendente d "
				+ "on pc.codice_fiscale = d.codice_fiscale "
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk "
				+ "where pc.tipo_pagamento_fk = 'TG' and (d.codice_dipendente is null or g.tipo_grado_fk in ('UF', 'SU') or d.tipo_personale = 'C') "
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + " "
				+ "group by pc.data_pasto "
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryUFC); 
		Query numUFCQuery = entityManager.createNativeQuery(queryUFC);
		List<Object[]> listOfResultsPastiUFC = numUFCQuery.getResultList();

		for(Object[] obj : listOfResultsPastiUFC)
		{
			FEPastiDC4USC fePastiDC4USC = new FEPastiDC4USC();

			Integer numpranziUSC = ((BigInteger) obj[1]).intValue();
			Integer numCeneUSC = ((BigInteger) obj[2]).intValue();
			Date dataReport = ((Date) obj[0]);

			fePastiDC4USC.setNPranziT1(String.valueOf(numpranziUSC));
			fePastiDC4USC.setNCeneT1(String.valueOf(numCeneUSC));
			fePastiDC4USC.setData(simpleDateFormat.format(dataReport));
			listaPastiUFC.add(fePastiDC4USC);		
		}

		//Pasti Graduati
		String queryGraduati = "select "
				+ "pc.data_pasto, "
				+ "sum(case when pc.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE, "
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA "
				+ "from pasti_consumati pc left join dipendente d "
				+ "on pc.codice_fiscale = d.codice_fiscale "
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk "
				+ "where pc.tipo_pagamento_fk = 'TG' and g.tipo_grado_fk = 'GT' "
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + " "
				+ "group by pc.data_pasto "
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryGraduati); 
		Query graduatiQuery = entityManager.createNativeQuery(queryGraduati);
		List<Object[]> listOfResultsPastiGraduati = graduatiQuery.getResultList();

		for(Object[] obj : listOfResultsPastiGraduati)
		{
			FEPastiDC4Graduati fePastiDC4Graduati = new FEPastiDC4Graduati();

			Integer numColazioniGraduati = ((BigInteger) obj[1]).intValue();
			Integer numPranziGraduati = ((BigInteger) obj[2]).intValue();
			Integer numCeneGraduati = ((BigInteger) obj[3]).intValue();

			Date dataReport = (Date) obj[0];

			//Lista Passata
			fePastiDC4Graduati.setNColazioniT2(String.valueOf(numColazioniGraduati));
			fePastiDC4Graduati.setNPranziT2(String.valueOf(numPranziGraduati));
			fePastiDC4Graduati.setNCeneT2(String.valueOf(numCeneGraduati));
			fePastiDC4Graduati.setGiorno(simpleDateFormat.format(dataReport));
			listaPastiGraduati.add(fePastiDC4Graduati);
		}

		sendObjList.setListaUFC(listaPastiUFC);
		sendObjList.setListaGraduati(listaPastiGraduati);

		logger.info("Lista creata con successo");
		return sendObjList;
	}

	/* Download documento DC4 Allegato C */
	public List<DC4TabellaAllegatoCDTO> downloadDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO)
	{
		logger.info("Accesso a richiestaDocumentoDC4AllegatoC classe ReportDAOImpl");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Map<String, DC4TabellaAllegatoCDTO> map = new HashMap<String, DC4TabellaAllegatoCDTO>();

		//Pasti UFC	
		String giornoGraduati = "'%";
		String dataTotale = giornoGraduati.concat("-" + dc4RichiestaDTO.getMese() + "-" + dc4RichiestaDTO.getAnno() + "'");

		if(StringUtils.isBlank(dataTotale))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		String queryUFC = "select "
				+ "pc.data_pasto, "
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA "
				+ "from pasti_consumati pc left join dipendente d "
				+ "on pc.codice_fiscale = d.codice_fiscale "
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk "
				+ "where pc.tipo_pagamento_fk = 'TG' and (d.codice_dipendente is null or g.tipo_grado_fk in ('UF', 'SU') or d.tipo_personale = 'C') "
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + " "
				+ "group by pc.data_pasto "
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryUFC); 
		Query numUFCQuery = entityManager.createNativeQuery(queryUFC);
		List<Object[]> listOfResultsPastiUFC = numUFCQuery.getResultList();

		for(Object[] obj : listOfResultsPastiUFC)
		{
			FEPastiDC4USC fePastiDC4USC = new FEPastiDC4USC();
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
		String queryGraduati = "select "
				+ "pc.data_pasto, "
				+ "sum(case when pc.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE, "
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA "
				+ "from pasti_consumati pc left join dipendente d "
				+ "on pc.codice_fiscale = d.codice_fiscale "
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk "
				+ "where pc.tipo_pagamento_fk = 'TG' and g.tipo_grado_fk = 'GT' "
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + " "
				+ "group by pc.data_pasto "
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryGraduati); 
		Query graduatiQuery = entityManager.createNativeQuery(queryGraduati);
		List<Object[]> listOfResultsPastiGraduati = graduatiQuery.getResultList();

		for(Object[] obj : listOfResultsPastiGraduati)
		{
			//FEPastiDC4Graduati fePastiDC4Graduati = new FEPastiDC4Graduati();

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

	/* Download documento DC4 Allegato C Ufficiali*/
	@Override
	public List<DC4TabellaAllegatoCDTO> downloadDC4AllegatoCUfficiali(DC4RichiestaDTO dc4RichiestaDTO)
	{
		logger.info("Accesso a downloadDC4AllegatoCUfficiali classe ReportDAOImpl");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Map<String, DC4TabellaAllegatoCDTO> map = new HashMap<String, DC4TabellaAllegatoCDTO>();

		//Pasti UFC	
		String giornoGraduati = "'%";
		String dataTotale = giornoGraduati.concat("-" + dc4RichiestaDTO.getMese() + "-" + dc4RichiestaDTO.getAnno() + "'");

		if(StringUtils.isBlank(dataTotale))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		String queryUFC = "select "
				+ "pc.data_pasto, "
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA "
				+ "from pasti_consumati pc left join dipendente d "
				+ "on pc.codice_fiscale = d.codice_fiscale "
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk "
				+ "where pc.tipo_pagamento_fk = 'TG' and (d.codice_dipendente is null or g.tipo_grado_fk in ('UF', 'SU') or d.tipo_personale = 'C') "
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + " "
				+ "group by pc.data_pasto "
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryUFC); 
		Query numUFCQuery = entityManager.createNativeQuery(queryUFC);
		List<Object[]> listOfResultsPastiUFC = numUFCQuery.getResultList();

		for(Object[] obj : listOfResultsPastiUFC)
		{
			//FEPastiDC4USC fePastiDC4USC = new FEPastiDC4USC();
			DC4TabellaAllegatoCDTO dc4AllC = new DC4TabellaAllegatoCDTO();

			Integer numpranziUSC = ((BigInteger) obj[1]).intValue();
			dc4AllC.setNumpranziUSC(numpranziUSC);
			Integer numCeneUSC = ((BigInteger) obj[2]).intValue();
			dc4AllC.setNumCeneUSC(numCeneUSC);

			Date dataReport = ((Date) obj[0]);
			dc4AllC.setGiorno(simpleDateFormat.format(dataReport));

			map.put((String) simpleDateFormat.format(dataReport),dc4AllC); 
		}

		List<DC4TabellaAllegatoCDTO> listaDC4TabellaAllegatoCDTO = map.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());

		logger.info("Lista creata con successo");
		return listaDC4TabellaAllegatoCDTO;
	}

	/* Download documento DC4 Allegato C Graduati*/
	@Override
	public List<DC4TabellaAllegatoCDTO> downloadDC4AllegatoCGraduati(DC4RichiestaDTO dc4RichiestaDTO)
	{
		logger.info("Accesso a downloadDC4AllegatoCUfficiali classe ReportDAOImpl");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Map<String, DC4TabellaAllegatoCDTO> map = new HashMap<String, DC4TabellaAllegatoCDTO>();

		String giornoGraduati = "'%";
		String dataTotale = giornoGraduati.concat("-" + dc4RichiestaDTO.getMese() + "-" + dc4RichiestaDTO.getAnno() + "'");

		//Pasti Graduati
		String queryGraduati = "select "
				+ "pc.data_pasto, "
				+ "sum(case when pc.tipo_pasto_fk = 1 then 1 else 0 end) as COLAZIONE, "
				+ "sum(case when pc.tipo_pasto_fk = 2 then 1 else 0 end) as PRANZO, "
				+ "sum(case when pc.tipo_pasto_fk = 3 then 1 else 0 end) as CENA "
				+ "from pasti_consumati pc left join dipendente d "
				+ "on pc.codice_fiscale = d.codice_fiscale "
				+ "left join grado g on d.grado = g.shsgra_cod_uid_pk "
				+ "where pc.tipo_pagamento_fk = 'TG' and g.tipo_grado_fk = 'GT' "
				+ "and to_char(pc.data_pasto, 'DD-MM-YYYY') like " + dataTotale + " "
				+ "group by pc.data_pasto "
				+ "order by pc.data_pasto";

		logger.info("Esecuzione query: " + queryGraduati); 
		Query graduatiQuery = entityManager.createNativeQuery(queryGraduati);
		List<Object[]> listOfResultsPastiGraduati = graduatiQuery.getResultList();

		for(Object[] obj : listOfResultsPastiGraduati)
		{
			//FEPastiDC4Graduati fePastiDC4Graduati = new FEPastiDC4Graduati();
			DC4TabellaAllegatoCDTO dc4AllC = new DC4TabellaAllegatoCDTO();

			Integer numColazioniGraduati = ((BigInteger) obj[1]).intValue();
			Integer numPranziGraduati = ((BigInteger) obj[2]).intValue();
			Integer numCeneGraduati = ((BigInteger) obj[3]).intValue();

			Date dataReport = (Date) obj[0];

			dc4AllC.setGiorno(simpleDateFormat.format(dataReport));
			dc4AllC.setNumColazioniGraduati(numColazioniGraduati);
			dc4AllC.setNumPranziGraduati(numPranziGraduati);
			dc4AllC.setNumCeneGraduati(numCeneGraduati);		

			map.put((String) simpleDateFormat.format(dataReport),dc4AllC); 
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

	/* Aggiungi una nuova Firma */
	@Override
	public int createNuovaFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException 
	{
		logger.info("Accesso al servizio createNuovaFirma");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		FirmaQuodidiana firmaQuodidiana = new FirmaQuodidiana();

		logger.info("Controllo esistenza ente in corso...");
		Optional<Ente> optionalEnte = enteRepository.findById(firmaQuotidianaDC4DTO.getIdEnte());

		if(!optionalEnte.isPresent())
			throw new GesevException("Non è stato possibile creare la Firma, ente non presente");

		logger.info("Creazione firma in corso...");
		try 
		{
			firmaQuodidiana.setDataFirma(simpleDateFormat.parse(firmaQuotidianaDC4DTO.getDataFirma()));
			firmaQuodidiana.setIdOperatore(firmaQuotidianaDC4DTO.getIdOperatore());
			firmaQuodidiana.setEnte(optionalEnte.get());
			firmaQuodidianaRepository.save(firmaQuodidiana);
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio createTipoDerrata" + exc);
			throw new GesevException("Non è stato possibile inserire la nuova firma" + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Creazione firma avvenuta con successo");
		return 1;
	}

	/* Cancella un Firma */
	@Override
	public int deleteFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException
	{
		logger.info("Accesso al servizio createNuovaFirma");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		logger.info("Cancellazione firma in corso...");
		try
		{
			Date dataDelete = simpleDateFormat.parse(firmaQuotidianaDC4DTO.getDataFirma());
			firmaQuodidianaRepository.deleteByValues(firmaQuotidianaDC4DTO.getIdEnte(), 
					firmaQuotidianaDC4DTO.getIdOperatore(), dataDelete);
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio deleteFirma" + exc);
			throw new GesevException("Non è stato possibile cancellare la nuova firma" + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Cancellazione avvenuta con successo");
		return 1;
	}

	/* Richiesta documento DC1 Prenotati */
	@Override
	public SendListaDC1Prenotati richiestaDocumentoDC1Prenotati(DC4RichiestaDTO dc4RichiestaDTO,
			SendListaDC1Prenotati sendObjList) throws ParseException 
	{
		logger.info("Accesso a richiestaDocumentoDC1Prenotati classe ReportDAOImpl");

		SendListaDC1Prenotati sDc1 = new SendListaDC1Prenotati();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		List<SendListaDC1Prenotati> listaDC1Prenotati = new ArrayList<>();

		String giorno = dc4RichiestaDTO.getGiorno();
		String dataTotale = giorno.concat("-" + dc4RichiestaDTO.getMese() + "-" + dc4RichiestaDTO.getAnno() + "'");

		String dataYYYYmmDD = "'" + dc4RichiestaDTO.getAnno().concat("-" + dc4RichiestaDTO.getMese().concat("-" + dc4RichiestaDTO.getGiorno()) + "'");

		String dataCorretta = dc4RichiestaDTO.getAnno().concat("-" + dc4RichiestaDTO.getMese().concat("-" + dc4RichiestaDTO.getGiorno()));
		Date giornoDatato = simpleDateFormat.parse(dataCorretta);

		//Aventi Diritto Militari e Non
		String queryAventiDirittoMilitari = "select\r\n"
				+ "sum(case when d.tipo_personale = 'M' then 1 else 0 end) as NUM_MIL,\r\n"
				+ "count(*) as TOT_DIP\r\n"
				+ "from dipendente d\r\n"
				+ "where ente_appartenenza  = :idEnte ";


		logger.info("Esecuzione query: " + queryAventiDirittoMilitari); 
		Query aventiDirittoMilitariQuery = entityManager.createNativeQuery(queryAventiDirittoMilitari);

		aventiDirittoMilitariQuery = aventiDirittoMilitariQuery.setParameter("idEnte", dc4RichiestaDTO.getIdEnte());

		List<Object[]> listAventiDirittoMilitari = aventiDirittoMilitariQuery.getResultList();
		for(Object[] obj : listAventiDirittoMilitari)
		{
			Integer aventiDirittoMilitari = ((BigInteger) obj[0]).intValue();
			sDc1.setAventiDirittoMilitari(aventiDirittoMilitari == null ? 0 : aventiDirittoMilitari);
			Integer aventiDiritto = ((BigInteger) obj[1]).intValue();
			sDc1.setAventiDiritto(aventiDiritto == null ? 0 : aventiDiritto);
		}

		if(StringUtils.isBlank(dataTotale))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		String queryDC1Prenotati = "select\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 1 and p.tipo_personale = 'M') then 1 else 0 end ) as ORD_COL_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 2 and p.tipo_personale = 'M') then 1 else 0 end ) as ORD_PRA_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 3 and p.tipo_personale = 'M') then 1 else 0 end ) as ORD_CEN_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 1 and p.tipo_personale = 'M') then 1 else 0 end ) as MED_COL_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 2 and p.tipo_personale = 'M') then 1 else 0 end ) as MED_PRA_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 3 and p.tipo_personale = 'M') then 1 else 0 end ) as MED_CEN_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 1 and p.tipo_personale = 'M') then 1 else 0 end ) as PES_COL_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 2 and p.tipo_personale = 'M') then 1 else 0 end ) as PES_PRA_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 3 and p.tipo_personale = 'M') then 1 else 0 end ) as PES_CEN_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'C' and p.tipo_personale = 'M') then 1 else 0 end ) as CBT_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as ORD_COL_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as ORD_PRA_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as ORD_CEN_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as MED_COL_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as MED_PRA_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as MED_CEN_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as PES_COL_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as PES_PRA_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as PES_CEN_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'C' and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as CBT_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as ORD_COL_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as ORD_PRA_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as ORD_CEN_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as MED_COL_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as MED_PRA_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as MED_CEN_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as PES_COL_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as PES_PRA_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as PES_CEN_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'C' and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as CBT_TO,\r\n"
				+ "sum(case when p.specchio_flag = 'Y' and p.tipo_personale = 'M' then 1 else 0 end) SPECCHIO_MIL,\r\n"
				+ "sum(case when p.col_obbligatoria_flag  = 'Y' and p.tipo_personale = 'M' then 1 else 0 end) COL_OBBL_MIL,\r\n"
				+ "sum(case when p.specchio_flag = 'Y' and p.tipo_pagamento_fk = 'TG' then 1 else 0 end) SPECCHIO_TG,\r\n"
				+ "sum(case when p.col_obbligatoria_flag  = 'Y' and p.tipo_pagamento_fk = 'TG' then 1 else 0 end) COL_OBBL_TG,\r\n"
				+ "sum(case when p.specchio_flag = 'Y' and p.tipo_pagamento_fk = 'TO' then 1 else 0 end) SPECCHIO_TO,\r\n"
				+ "sum(case when p.col_obbligatoria_flag  = 'Y' and p.tipo_pagamento_fk = 'TO' then 1 else 0 end) COL_OBBL_TO "
				+ "from prenotazione p left join mensa m on p.identificativo_mensa_fk = m.codice_mensa\r\n"
				+ "where m.ente_fk = :idEnte and p.data_prenotazione = :giornoDatato ";

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			queryDC1Prenotati = queryDC1Prenotati + " and p.identificativo_sistema_fk = :idPersonale ";

		logger.info("Esecuzione query: " + queryDC1Prenotati); 
		Query dc1PrenotatiQuery = entityManager.createNativeQuery(queryDC1Prenotati);

		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idEnte", dc4RichiestaDTO.getIdEnte());
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("giornoDatato", giornoDatato);

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idPersonale", dc4RichiestaDTO.getSistemaPersonale());

		logger.info("Esecuzione query: " + queryDC1Prenotati + " " + dataYYYYmmDD);
		List<Object[]> listOfResultsDC1Prenotati = dc1PrenotatiQuery.getResultList();
		logger.info("Esecuzione query: " + queryDC1Prenotati);
		//Ente
		Optional<Ente> optionalEnte = enteRepository.findById(dc4RichiestaDTO.getIdEnte());

		if(!optionalEnte.isPresent())
			throw new GesevException("Non è stato possibile creare il report, ente non presente");

		sDc1.setDescrizioneEnte(optionalEnte.get().getDescrizioneEnte());

		//Assegnazione Campi Query
		for(Object[] obj : listOfResultsDC1Prenotati)
		{
			Integer ordColMil = ((BigInteger) obj[0]).intValue();
			sDc1.setOrdColMil(ordColMil == null ? 0 : ordColMil);
			Integer ordPraMil = ((BigInteger) obj[1]).intValue();
			sDc1.setOrdPraMil(ordPraMil == null ? 0 : ordPraMil);
			Integer ordCenMil = ((BigInteger) obj[2]).intValue();
			sDc1.setOrdCenMil(ordCenMil == null ? 0 : ordCenMil);
			Integer medColMil = ((BigInteger) obj[3]).intValue();
			sDc1.setMedColMil(medColMil == null ? 0 : medColMil);
			Integer medPraMil = ((BigInteger) obj[4]).intValue();
			sDc1.setMedPraMil(medPraMil == null ? 0 : medPraMil);
			Integer medCenMil = ((BigInteger) obj[5]).intValue();
			sDc1.setMedCenMil(medCenMil == null ? 0 : medCenMil);
			Integer pesColMil = ((BigInteger) obj[6]).intValue();
			sDc1.setPesColMil(pesColMil == null ? 0 : pesColMil);
			Integer pesPraMil = ((BigInteger) obj[7]).intValue();
			sDc1.setPesPraMil(pesPraMil == null ? 0 : pesPraMil);
			Integer pesCenMil = ((BigInteger) obj[8]).intValue();
			sDc1.setPesCenMil(pesCenMil == null ? 0 : pesCenMil);
			Integer cbtMil = ((BigInteger) obj[9]).intValue();
			sDc1.setCbtMil(cbtMil == null ? 0 : cbtMil);

			Integer ordColTg = ((BigInteger) obj[10]).intValue();
			sDc1.setOrdColTg(ordColTg == null ? 0 : ordColTg);
			Integer ordPraTg = ((BigInteger) obj[11]).intValue();
			sDc1.setOrdPraTg(ordPraTg == null ? 0 : ordPraTg);
			Integer ordCenTg = ((BigInteger) obj[12]).intValue();
			sDc1.setOrdCenTg(ordCenTg == null ? 0 : ordCenTg);
			Integer medColTg = ((BigInteger) obj[13]).intValue();
			sDc1.setMedColTg(medColTg == null ? 0 : medColTg);
			Integer medPraTg = ((BigInteger) obj[14]).intValue();
			sDc1.setMedPraTg(medPraTg == null ? 0 : medPraTg);
			Integer medCenTg = ((BigInteger) obj[15]).intValue();
			sDc1.setMedCenTg(medCenTg == null ? 0 : medCenTg);
			Integer pesColTg = ((BigInteger) obj[16]).intValue();
			sDc1.setPesColTg(pesColTg == null ? 0 : pesColTg);
			Integer pesPraTg = ((BigInteger) obj[17]).intValue();
			sDc1.setPesPraTg(pesPraTg == null ? 0 : pesPraTg);
			Integer pesCenTg = ((BigInteger) obj[18]).intValue();
			sDc1.setPesCenTg(pesCenTg == null ? 0 : pesCenTg);
			Integer cbtTg = ((BigInteger) obj[19]).intValue();
			sDc1.setCbtTg(cbtTg == null ? 0 : cbtTg);

			Integer ordColTo = ((BigInteger) obj[20]).intValue();
			sDc1.setOrdColTo(ordColTo == null ? 0 : ordColTo);
			Integer ordPraTo = ((BigInteger) obj[21]).intValue();
			sDc1.setOrdPraTo(ordPraTo == null ? 0 : ordPraTo);
			Integer ordCenTo = ((BigInteger) obj[22]).intValue();
			sDc1.setOrdCenTo(ordCenTo == null ? 0 : ordCenTo);
			Integer medColTo = ((BigInteger) obj[23]).intValue();
			sDc1.setMedColTo(medColTo == null ? 0 : medColTo);
			Integer medPraTo = ((BigInteger) obj[24]).intValue();
			sDc1.setMedPraTo(medPraTo == null ? 0 : medPraTo);
			Integer medCenTo = ((BigInteger) obj[25]).intValue();
			sDc1.setMedCenTo(medCenTo == null ? 0 : medCenTo);
			Integer pesColTo = ((BigInteger) obj[26]).intValue();
			sDc1.setPesColTo(pesColTo == null ? 0 : pesColTo);
			Integer pesPraTo = ((BigInteger) obj[27]).intValue();
			sDc1.setPesPraTo(pesPraTo == null ? 0 : pesPraTo);
			Integer pesCenTo = ((BigInteger) obj[28]).intValue();
			sDc1.setPesCenTo(pesCenTo == null ? 0 : pesCenTo);
			Integer cbtTo = ((BigInteger) obj[29]).intValue();
			sDc1.setCbtTo(cbtTo == null ? 0 : cbtTo);
			
			Integer specchioMil = ((BigInteger) obj[30]).intValue();
			sDc1.setSpecchioMil(specchioMil == null ? 0 : specchioMil);
			Integer colazioneObblMil= ((BigInteger) obj[31]).intValue();
			sDc1.setColazioneObblMil(colazioneObblMil == null ? 0 : colazioneObblMil);
			Integer specchioTg = ((BigInteger) obj[32]).intValue();
			sDc1.setSpecchioTg(specchioTg == null ? 0 : specchioTg);
			Integer colazioneObblTg= ((BigInteger) obj[33]).intValue();
			sDc1.setColazioneObblTg(colazioneObblTg == null ? 0 : colazioneObblTg);		
			Integer specchioTo = ((BigInteger) obj[34]).intValue();
			sDc1.setSpecchioTo(specchioTo == null ? 0 : specchioTo);
			Integer colazioneObblTo= ((BigInteger) obj[35]).intValue();
			sDc1.setColazioneObblTo(colazioneObblTo == null ? 0 : colazioneObblTo);
			
			listaDC1Prenotati.add(sDc1);
		}

		logger.info("Lista creata con successo");
		sendObjList = sDc1;
		return sDc1;
	}

	@Override
	public List<FirmeDC4> richiestaFirmeDC1(DC4RichiestaDTO dc4RichiestaDTO) 
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

	/* Richiesta documento DC1 Consumati */
	@Override
	public SendListaDC1Prenotati richiestaDocumentoDC1Consumati(DC4RichiestaDTO dc4RichiestaDTO,
			SendListaDC1Prenotati sendObjList) throws ParseException 
	{
		logger.info("Accesso a richiestaDocumentoDC1Consumati classe ReportDAOImpl");

		SendListaDC1Prenotati sDc1 = new SendListaDC1Prenotati();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		List<SendListaDC1Prenotati> listaDC1Prenotati = new ArrayList<>();

		String giorno = dc4RichiestaDTO.getGiorno();
		String dataTotale = giorno.concat("-" + dc4RichiestaDTO.getMese() + "-" + dc4RichiestaDTO.getAnno() + "'");

		String dataYYYYmmDD = "'" + dc4RichiestaDTO.getAnno().concat("-" + dc4RichiestaDTO.getMese().concat("-" + dc4RichiestaDTO.getGiorno()) + "'");

		String dataCorretta = dc4RichiestaDTO.getAnno().concat("-" + dc4RichiestaDTO.getMese().concat("-" + dc4RichiestaDTO.getGiorno()));
		Date giornoDatato = simpleDateFormat.parse(dataCorretta);

		//Aventi Diritto Militari
		String queryAventiDirittoMilitari = "select distinct fe.num_dipendenti \r\n"
				+ "from forza_effettiva fe \r\n"
				+ "left join dipendente d \r\n"
				+ "on fe.ente_fk = :idEnte \r\n"
				+ "and fe.data_riferimento = :giornoDatato \r\n"
				+ "where d.tipo_personale = 'M'";


		logger.info("Esecuzione query: " + queryAventiDirittoMilitari); 
		Query aventiDirittoMilitariQuery = entityManager.createNativeQuery(queryAventiDirittoMilitari);

		aventiDirittoMilitariQuery = aventiDirittoMilitariQuery.setParameter("idEnte", dc4RichiestaDTO.getIdEnte());
		aventiDirittoMilitariQuery = aventiDirittoMilitariQuery.setParameter("giornoDatato", giornoDatato);

		Integer aventiDirittoMilitari = (Integer) aventiDirittoMilitariQuery.getSingleResult();
		sDc1.setAventiDirittoMilitari(aventiDirittoMilitari);

		int aventiDiritto = forzaEffettivaRepository.aventiDiritto(dc4RichiestaDTO.getIdEnte(), giornoDatato);

		if(StringUtils.isBlank(dataTotale))
			throw new GesevException("Impossibile generare il documento DC4, mese non valido", HttpStatus.BAD_REQUEST);

		String queryDC1Prenotati = "select\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 1 and p.tipo_personale = 'M') then 1 else 0 end ) as ORD_COL_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 2 and p.tipo_personale = 'M') then 1 else 0 end ) as ORD_PRA_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 3 and p.tipo_personale = 'M') then 1 else 0 end ) as ORD_CEN_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 1 and p.tipo_personale = 'M') then 1 else 0 end ) as MED_COL_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 2 and p.tipo_personale = 'M') then 1 else 0 end ) as MED_PRA_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 3 and p.tipo_personale = 'M') then 1 else 0 end ) as MED_CEN_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 1 and p.tipo_personale = 'M') then 1 else 0 end ) as PES_COL_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 2 and p.tipo_personale = 'M') then 1 else 0 end ) as PES_PRA_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 3 and p.tipo_personale = 'M') then 1 else 0 end ) as PES_CEN_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'C' and p.tipo_personale = 'M') then 1 else 0 end ) as CBT_MIL,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as ORD_COL_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as ORD_PRA_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as ORD_CEN_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as MED_COL_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as MED_PRA_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as MED_CEN_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as PES_COL_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as PES_PRA_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as PES_CEN_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'C' and p.tipo_pagamento_fk = 'TG') then 1 else 0 end ) as CBT_TG,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as ORD_COL_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as ORD_PRA_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'O' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as ORD_CEN_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as MED_COL_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as MED_PRA_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'M' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as MED_CEN_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 1 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as PES_COL_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 2 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as PES_PRA_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'P' and p.tipo_pasto_fk = 3 and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as PES_CEN_TO,\r\n"
				+ "sum(case when (p.tipo_razione_fk = 'C' and p.tipo_pagamento_fk = 'TO') then 1 else 0 end ) as CBT_TO\r\n"
				+ "from pasti_consumati p left join mensa m on p.mensa_fk  = m.codice_mensa\r\n"
				+ "where m.ente_fk = :idEnte and p.data_pasto  = :giornoDatato ";

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			queryDC1Prenotati = queryDC1Prenotati + " and p.identificativo_sistema_fk = :idPersonale ";

		logger.info("Esecuzione query: " + queryDC1Prenotati); 
		Query dc1PrenotatiQuery = entityManager.createNativeQuery(queryDC1Prenotati);

		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idEnte", dc4RichiestaDTO.getIdEnte());
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("giornoDatato", giornoDatato);

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idPersonale", dc4RichiestaDTO.getSistemaPersonale());

		logger.info("Esecuzione query: " + queryDC1Prenotati);
		List<Object[]> listOfResultsDC1Prenotati = dc1PrenotatiQuery.getResultList();

		//Ente
		Optional<Ente> optionalEnte = enteRepository.findById(dc4RichiestaDTO.getIdEnte());

		if(!optionalEnte.isPresent())
			throw new GesevException("Non è stato possibile creare il report, ente non presente");

		sDc1.setDescrizioneEnte(optionalEnte.get().getDescrizioneEnte());

		//Assegnazione Campi Query
		for(Object[] obj : listOfResultsDC1Prenotati)
		{
			Integer ordColMil = ((BigInteger) obj[0]).intValue();
			sDc1.setOrdColMil(ordColMil == null ? 0 : ordColMil);
			Integer ordPraMil = ((BigInteger) obj[1]).intValue();
			sDc1.setOrdPraMil(ordPraMil == null ? 0 : ordPraMil);
			Integer ordCenMil = ((BigInteger) obj[2]).intValue();
			sDc1.setOrdCenMil(ordCenMil == null ? 0 : ordCenMil);
			Integer medColMil = ((BigInteger) obj[3]).intValue();
			sDc1.setMedColMil(medColMil == null ? 0 : medColMil);
			Integer medPraMil = ((BigInteger) obj[4]).intValue();
			sDc1.setMedPraMil(medPraMil == null ? 0 : medPraMil);
			Integer medCenMil = ((BigInteger) obj[5]).intValue();
			sDc1.setMedCenMil(medCenMil == null ? 0 : medCenMil);
			Integer pesColMil = ((BigInteger) obj[6]).intValue();
			sDc1.setPesColMil(pesColMil == null ? 0 : pesColMil);
			Integer pesPraMil = ((BigInteger) obj[7]).intValue();
			sDc1.setPesPraMil(pesPraMil == null ? 0 : pesPraMil);
			Integer pesCenMil = ((BigInteger) obj[8]).intValue();
			sDc1.setPesCenMil(pesCenMil == null ? 0 : pesCenMil);
			Integer cbtMil = ((BigInteger) obj[9]).intValue();
			sDc1.setCbtMil(cbtMil == null ? 0 : cbtMil);

			Integer ordColTg = ((BigInteger) obj[10]).intValue();
			sDc1.setOrdColTg(ordColTg == null ? 0 : ordColTg);
			Integer ordPraTg = ((BigInteger) obj[11]).intValue();
			sDc1.setOrdPraTg(ordPraTg == null ? 0 : ordPraTg);
			Integer ordCenTg = ((BigInteger) obj[12]).intValue();
			sDc1.setOrdCenTg(ordCenTg == null ? 0 : ordCenTg);
			Integer medColTg = ((BigInteger) obj[13]).intValue();
			sDc1.setMedColTg(medColTg == null ? 0 : medColTg);
			Integer medPraTg = ((BigInteger) obj[14]).intValue();
			sDc1.setMedPraTg(medPraTg == null ? 0 : medPraTg);
			Integer medCenTg = ((BigInteger) obj[15]).intValue();
			sDc1.setMedCenTg(medCenTg == null ? 0 : medCenTg);
			Integer pesColTg = ((BigInteger) obj[16]).intValue();
			sDc1.setPesColTg(pesColTg == null ? 0 : pesColTg);
			Integer pesPraTg = ((BigInteger) obj[17]).intValue();
			sDc1.setPesPraTg(pesPraTg == null ? 0 : pesPraTg);
			Integer pesCenTg = ((BigInteger) obj[18]).intValue();
			sDc1.setPesCenTg(pesCenTg == null ? 0 : pesCenTg);
			Integer cbtTg = ((BigInteger) obj[19]).intValue();
			sDc1.setCbtTg(cbtTg == null ? 0 : cbtTg);

			Integer ordColTo = ((BigInteger) obj[20]).intValue();
			sDc1.setOrdColTo(ordColTo == null ? 0 : ordColTo);
			Integer ordPraTo = ((BigInteger) obj[21]).intValue();
			sDc1.setOrdPraTo(ordPraTo == null ? 0 : ordPraTo);
			Integer ordCenTo = ((BigInteger) obj[22]).intValue();
			sDc1.setOrdCenTo(ordCenTo == null ? 0 : ordCenTo);
			Integer medColTo = ((BigInteger) obj[23]).intValue();
			sDc1.setMedColTo(medColTo == null ? 0 : medColTo);
			Integer medPraTo = ((BigInteger) obj[24]).intValue();
			sDc1.setMedPraTo(medPraTo == null ? 0 : medPraTo);
			Integer medCenTo = ((BigInteger) obj[25]).intValue();
			sDc1.setMedCenTo(medCenTo == null ? 0 : medCenTo);
			Integer pesColTo = ((BigInteger) obj[26]).intValue();
			sDc1.setPesColTo(pesColTo == null ? 0 : pesColTo);
			Integer pesPraTo = ((BigInteger) obj[27]).intValue();
			sDc1.setPesPraTo(pesPraTo == null ? 0 : pesPraTo);
			Integer pesCenTo = ((BigInteger) obj[28]).intValue();
			sDc1.setPesCenTo(pesCenTo == null ? 0 : pesCenTo);
			Integer cbtTo = ((BigInteger) obj[29]).intValue();
			sDc1.setCbtTo(cbtTo == null ? 0 : cbtTo);

			sDc1.setAventiDiritto(aventiDiritto);
			

			listaDC1Prenotati.add(sDc1);
		}

		logger.info("Lista creata con successo");
		sendObjList = sDc1;
		return sDc1;
	}

	/* Richiesta documento DC1 Nominativo Numerica */
	@Override
	public DC1NomNumericaJasper richiestaDocumentoDC1NominativoNumerica(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException 
	{
		logger.info("Accesso a richiestaDocumentoDC1NominativoNumerica classe ReportDAOImpl");

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

		DC1NomNumericaJasper dc1NomNum = new DC1NomNumericaJasper();

		String queryDC1Prenotati = "select\r\n"
				+ "p.identificativo_sistema_fk,\r\n"
				+ "sum(case when p.tipo_grado_fk = 'UF' and p.flag_cestino = 'N' then 1 else 0 end) as UFFICIALI,\r\n"
				+ "sum(case when p.tipo_grado_fk = 'SU' and p.flag_cestino = 'N' then 1 else 0 end) as SOTTO_UFFICIALI,\r\n"
				+ "sum(case when p.tipo_grado_fk = 'GT' and p.flag_cestino = 'N' then 1 else 0 end) as GRADUATI_TRUPPA,\r\n"
				+ "sum(case when (p.tipo_personale = 'C' or p.tipo_personale is null) and p.flag_cestino = 'N' then 1 else 0 end) as CIVILI,\r\n"
				+ "SUM(case when p.flag_cestino = 'Y' then 1 else 0 end) as cestini,\r\n"
				+ "count(*) as totale\r\n"
				+ "from prenotazione p left join mensa m on p.identificativo_mensa_fk = m.codice_mensa\r\n"
				+ "where p.data_prenotazione = :giornoDatato and m.ente_fk = :idEnte and p.tipo_pasto_fk = :tipoPasto ";

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			queryDC1Prenotati = queryDC1Prenotati + " and p.identificativo_sistema_fk = :idPersonale ";

		queryDC1Prenotati = queryDC1Prenotati + "group by p.identificativo_sistema_fk";

		logger.info("Esecuzione query: " + queryDC1Prenotati); 
		Query dc1PrenotatiQuery = entityManager.createNativeQuery(queryDC1Prenotati);

		//Parametri
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idEnte", dc4RichiestaDTO.getIdEnte());
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("giornoDatato", giornoDatato);
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("tipoPasto", dc4RichiestaDTO.getTipoPasto());

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idPersonale", dc4RichiestaDTO.getSistemaPersonale());

		List<Object[]> listOfResultsDC1Prenotati = dc1PrenotatiQuery.getResultList();

		//Assegnazione Campi Query
		for(Object[] obj : listOfResultsDC1Prenotati)
		{
			Integer ufficiali = ((BigInteger) obj[1]).intValue();
			Integer sottoUff = ((BigInteger) obj[2]).intValue();
			Integer graduati = ((BigInteger) obj[3]).intValue();
			Integer civili = ((BigInteger) obj[4]).intValue();
			Integer cestini = ((BigInteger) obj[5]).intValue();
			Integer totale = ((BigInteger) obj[6]).intValue();

			dc1NomNum.setSistemaGestione((String) obj[0]);
			dc1NomNum.setUfficiali(ufficiali);
			dc1NomNum.setSottoUfficiali(sottoUff);
			dc1NomNum.setGraduati(graduati);
			dc1NomNum.setCivili(civili);
			dc1NomNum.setCestini(cestini);
			dc1NomNum.setTotale(totale - cestini);
		}

		logger.info("Assegnazione eseguita con successo");
		return dc1NomNum;
	}

	/* Richiesta documento DC1 Nominativo */
	@Override
	public List<DC1NomJasper> richiestaDocumentoDC1Nominativo(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException 
	{
		logger.info("Accesso a richiestaDocumentoDC1Nominativo classe ReportDAOImpl");

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

		List<DC1NomJasper> listaDC1NomJasper = new ArrayList<>();

		String queryDC1Prenotati = "select\r\n"
				+ "e.descrizione_ente,\r\n"
				+ "p.denominazione_unita_funzionale,\r\n"
				+ "case when p.tipo_pagamento_fk = 'TO' then 'Titolo oneroso' else 'Titolo gratuito' end,\r\n"
				+ "g.descr_grado,\r\n"
				+ "p.nome,\r\n"
				+ "p.cognome,\r\n"
				+ "case when tipo_personale is null then 'SI' else 'NO' end esterno\r\n"
				+ "from prenotazione p left join mensa m on p.identificativo_mensa_fk = m.codice_mensa\r\n"
				+ "left join ente e on e.id_ente = m.ente_fk\r\n"
				+ "left join grado g on g.shsgra_cod_uid_pk = p.grado_fk\r\n"
				+ "where p.data_prenotazione = :giornoDatato and m.ente_fk = :idEnte and p.tipo_pasto_fk = :tipoPasto ";

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			queryDC1Prenotati = queryDC1Prenotati + " and p.identificativo_sistema_fk = :idPersonale ";

		logger.info("Esecuzione query: " + queryDC1Prenotati); 
		Query dc1PrenotatiQuery = entityManager.createNativeQuery(queryDC1Prenotati);

		//Parametri
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idEnte", dc4RichiestaDTO.getIdEnte());
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("giornoDatato", giornoDatato);
		dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("tipoPasto", dc4RichiestaDTO.getTipoPasto());

		if(!StringUtils.isBlank(dc4RichiestaDTO.getSistemaPersonale()))
			dc1PrenotatiQuery = dc1PrenotatiQuery.setParameter("idPersonale", dc4RichiestaDTO.getSistemaPersonale());

		List<Object[]> listOfResultsDC1Prenotati = dc1PrenotatiQuery.getResultList();

		//Assegnazione Campi Query
		for(Object[] obj : listOfResultsDC1Prenotati)
		{
			DC1NomJasper dc1Nom = new DC1NomJasper();

			dc1Nom.setEnte((String) obj[0]);
			dc1Nom.setUnitaFunzionale((String) obj[1]);
			dc1Nom.setTipoPagamento((String) obj[2]);
			dc1Nom.setGrado((String) obj[3]);
			dc1Nom.setNome((String) obj[4]);
			dc1Nom.setCognome((String) obj[5]);
			dc1Nom.setPersonaleEsterno((String) obj[6]);

			listaDC1NomJasper.add(dc1Nom);
		}

		logger.info("Assegnazione eseguita con successo");
		return listaDC1NomJasper;
	}

	/* Richiesta Menu del giorno */
	@Override
	public List<Pietanza> richiestaMenuDelGiorno(MenuDTO menuDTO) throws ParseException 
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date data = simpleDateFormat.parse(menuDTO.getDataMenu());
		List<Pietanza> listaPietanze = pietanzaRepository.findPietanzeByIdMenu(menuDTO.getIdMensa(), menuDTO.getTipoDieta(), menuDTO.getTipoPasto(), data);
		return listaPietanze;
	}

	/* Download Menu del giorno */
	@Override
	public List<Pietanza> richiestaTuttePietanze(MenuLeggeroDTO menuLeggeroDTO) throws ParseException
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date data = simpleDateFormat.parse(menuLeggeroDTO.getDataMenu());
		List<Pietanza> listaPietanze = pietanzaRepository.findAllPietanzeByIdMenu(menuLeggeroDTO.getIdMensa(), menuLeggeroDTO.getTipoDieta(),data);
		return listaPietanze;
	}

}
