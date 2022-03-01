package it.gesev.mensa.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.gesev.mensa.entity.AssReportRuoloMensa;
import it.gesev.mensa.entity.Report;
import it.gesev.mensa.repository.AssReportRuoloMensaRepository;
import it.gesev.mensa.repository.ReportRepository;

@Component
public class FirmaDAOImpl implements FirmaDAO {

	private static Logger logger = LoggerFactory.getLogger(FirmaDAOImpl.class);
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private AssReportRuoloMensaRepository assReportRuoloMensaRepository;
	
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

}
