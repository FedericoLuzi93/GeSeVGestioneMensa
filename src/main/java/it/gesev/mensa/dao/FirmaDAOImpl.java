package it.gesev.mensa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.mensa.dto.FirmaDTO;
import it.gesev.mensa.dto.OrdineFirmaDTO;
import it.gesev.mensa.entity.AssReportRuoloMensa;
import it.gesev.mensa.entity.Report;
import it.gesev.mensa.entity.RuoloMensa;
import it.gesev.mensa.entity.TipoReport;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssReportRuoloMensaRepository;
import it.gesev.mensa.repository.ReportRepository;
import it.gesev.mensa.repository.RuoloMensaRepository;
import it.gesev.mensa.repository.TipoReportRepository;

@Component
public class FirmaDAOImpl implements FirmaDAO {

	private static Logger logger = LoggerFactory.getLogger(FirmaDAOImpl.class);
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private AssReportRuoloMensaRepository assReportRuoloMensaRepository;
	
	@Autowired
	private TipoReportRepository tipoReportRepository;
	
	@Autowired
	private RuoloMensaRepository ruoloMensaRepository;
	
	@Override
	public List<Report> getListaReport(Integer tipoRecord) 
	{
		logger.info("Ricerca report" + (tipoRecord != null ? " con ID " + tipoRecord : "") + "...");
		
		List<Report> listaReport = tipoRecord == null ? reportRepository.findAll() : reportRepository.getReportByTipo(tipoRecord);
		
		logger.info("Trovati " + listaReport.size() + " elementi.");
		
		return listaReport;
	}

	@Override
	public List<AssReportRuoloMensa> getReportRuolo() 
	{
		logger.info("Ricerca associazioni tra report e ruoli...");
		
		List<AssReportRuoloMensa> listaAssociazioni = assReportRuoloMensaRepository.findAll();
		
		logger.info("Trovati " + listaAssociazioni.size() + " elementi.");
		
		return listaAssociazioni;

	}

	@Override
	public List<TipoReport> getTipiReport() {
		logger.info("Ricerca tipi report...");
		
		List<TipoReport> listaTipiReport = tipoReportRepository.findAll();
		
		logger.info("Trovati " + listaTipiReport.size() + " elementi.");
		
		return listaTipiReport;
	}

	@Override
	@Transactional
	public void registraFirme(FirmaDTO firma) throws ParseException 
	{
		logger.info("Registrazione firme...");
		
		if(firma.getIdReport() == null || firma.getListaForma() == null || firma.getListaForma().size() == 0)
			throw new GesevException("I dati forniti non sono corretti", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo presenza associazione report-firma...");
		int numeroFirme = assReportRuoloMensaRepository.getNumeroFirmeReport(firma.getIdReport());
		if(numeroFirme > 0)
			throw new GesevException("Report gia' associato ad elenco firme", HttpStatus.BAD_REQUEST);
		
		for(OrdineFirmaDTO ordine : firma.getListaForma())
		{
			Optional<Report> reportOpt = reportRepository.findById(firma.getIdReport());
			if(!reportOpt.isPresent())
				throw new GesevException("Identificativo del report " + firma.getIdReport() + " non e' valido.", HttpStatus.BAD_REQUEST);
			
			Optional<RuoloMensa> ruoloOpt = ruoloMensaRepository.findById(ordine.getIdRuolo());
			if(!ruoloOpt.isPresent())
				throw new GesevException("Identificativo del ruolo " + ordine.getIdRuolo() + " non e' valido.", HttpStatus.BAD_REQUEST);
			
			if(ordine.getOrdineFirma() == null || ordine.getOrdineFirma() < 1)
				throw new GesevException("Ordine firma " + ordine.getOrdineFirma() + " non valido.", HttpStatus.BAD_REQUEST);
			
			AssReportRuoloMensa associazione = new AssReportRuoloMensa();
			SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
			
			associazione.setDataFine(formatter.parse("9999-12-31"));
			associazione.setDataInizio(new Date());
			associazione.setOrdineFirma(ordine.getOrdineFirma());
			associazione.setReport(reportOpt.get());
			associazione.setRuoloMensa(ruoloOpt.get());
			
			assReportRuoloMensaRepository.save(associazione);
			
		}
		
		logger.info("Fine registrazione firme");
	}

	@Override
	public List<AssReportRuoloMensa> getRuoliReportByIdReport(String idReport) 
	{
		logger.info("Ricerca associazioni ruolo-report sulla base dell'ID del report...");
		
		if(StringUtils.isBlank(idReport))
			throw new GesevException("ID del report non valido", HttpStatus.BAD_REQUEST);
		
		List<AssReportRuoloMensa> listaAssociazioni = assReportRuoloMensaRepository.getAssociazioniByIdReport(idReport);
		logger.info("Trovati " + listaAssociazioni.size() + " elementi.");
		
		return listaAssociazioni;
	}

	@Override
	@Transactional
	public void modificaFirme(FirmaDTO firma) throws ParseException 
	{
		logger.info("Inizio modifica firme...");
		
		logger.info("Controllo presenza dati necessari...");
		if(StringUtils.isBlank(firma.getIdReport()))
			throw new GesevException("L'Id del report non e' valido", HttpStatus.BAD_REQUEST);
		
		logger.info("Cancellazione ruoli precedenti...");
		int numRigheCancellate = assReportRuoloMensaRepository.deleteByIdReport(firma.getIdReport());
		logger.info("Cancellate " + numRigheCancellate + " righe.");
		
		logger.info("Aggiornamento con nuovi dati...");
		
		registraFirme(firma);
		
		logger.info("Fine modifica firme...");		
	}

}
