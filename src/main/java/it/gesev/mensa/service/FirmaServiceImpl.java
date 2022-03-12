package it.gesev.mensa.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dao.FirmaDAO;
import it.gesev.mensa.dao.FirmaDAOImpl;
import it.gesev.mensa.dto.AssReportRuoloMensaDTO;
import it.gesev.mensa.dto.DettaglioReportDTO;
import it.gesev.mensa.dto.DipendenteDTO;
import it.gesev.mensa.dto.FirmaDTO;
import it.gesev.mensa.dto.ReportDTO;
import it.gesev.mensa.dto.TipoReportDTO;
import it.gesev.mensa.entity.AssReportRuoloMensa;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.Report;
import it.gesev.mensa.entity.TipoReport;

@Service
public class FirmaServiceImpl implements FirmaService
{
	private static Logger logger = LoggerFactory.getLogger(FirmaDAOImpl.class);
	
	@Autowired
	private FirmaDAO firmaDAO;
	
	@Override
	public DettaglioReportDTO getDettaglioReport(TipoReportDTO tipoReport) 
	{
		logger.info("Servizio per la generazione del dettaglio delle firme...");
		
		DettaglioReportDTO dettaglio = new DettaglioReportDTO();
		ModelMapper mapper = new ModelMapper();
		
		List<Report> listaReport = firmaDAO.getListaReport(tipoReport.getCodiceTipoReport());
//		List<AssReportRuoloMensa> listaAssociazioni = firmaDAO.getReportRuolo();
//		List<Report> listaReportAssociazioni = firmaDAO.selectReportInAssociazione();
		List<TipoReport> listaTipiReport = firmaDAO.getTipiReport();
		
		if(listaReport != null && listaReport.size() > 0)
		{
			List<ReportDTO> listaReportDTO = new ArrayList<>();
			for(Report report : listaReport)
			{
				ReportDTO reportDTO = mapper.map(report, ReportDTO.class);
				reportDTO.setHasFirma(report.getListaAssReportRuoloMensa() != null && report.getListaAssReportRuoloMensa().size() > 0);
				listaReportDTO.add(reportDTO);
			}
			
			dettaglio.setListaReport(listaReportDTO);
		}
		
//		if(listaAssociazioni != null && listaAssociazioni.size() > 0)
//		{
//			List<AssReportRuoloMensaDTO> listaAssociazioniDTO = new ArrayList<>();
//			for(AssReportRuoloMensa associazione : listaAssociazioni)
//			{
//				AssReportRuoloMensaDTO dto = new AssReportRuoloMensaDTO();
//				dto.setAssReportRuoloMensaId(associazione.getAssReportRuoloMensaId());
//				dto.setRuolo(associazione.getRuoloMensa() != null ? associazione.getRuoloMensa().getDescrizioneRuoloMensa() : null);
//				
//				listaAssociazioniDTO.add(dto);
//			}
//			
//			dettaglio.setListaAssociazioni(listaAssociazioniDTO);
//		}
//		
//		if(listaReportAssociazioni != null && listaReportAssociazioni.size() > 0)
//		{
//			List<ReportDTO> listaReportDTO = new ArrayList<>();
//			for(Report report : listaReportAssociazioni)
//			{
//				listaReportDTO.add(mapper.map(report, ReportDTO.class));
//			}
//			
//			dettaglio.setListaReportInAssociazioni(listaReportDTO);
//		}
//		
//		
		if(listaTipiReport != null && listaTipiReport.size() > 0)
		{
			List<TipoReportDTO> listaDTO = new ArrayList<>();
			for(TipoReport tipo : listaTipiReport)
				listaDTO.add(mapper.map(tipo, TipoReportDTO.class));
			
			dettaglio.setListaTipiReport(listaDTO);
		}
		
		return dettaglio;
	}

	@Override
	public void registraFirme(FirmaDTO firma) throws ParseException {
		logger.info("Servizio inserimento firme...");
		
		firmaDAO.registraFirme(firma);
		
	}

	@Override
	public List<AssReportRuoloMensaDTO> getRuoliReportByIdReport(String idReport) 
	{
		logger.info("Servizio ricerca ruoli-report sulla base dell'ID del report...");
		
		List<AssReportRuoloMensa> listaAssociazioni = firmaDAO.getRuoliReportByIdReport(idReport);
		List<AssReportRuoloMensaDTO> listaAssociazioniDTO = new ArrayList<>();
		
		if(listaAssociazioni != null && listaAssociazioni.size() > 0)
		{
			for(AssReportRuoloMensa associazione : listaAssociazioni)
			{
				AssReportRuoloMensaDTO associazioneDTO = new AssReportRuoloMensaDTO();
				associazioneDTO.setAssReportRuoloMensaId(associazione.getAssReportRuoloMensaId());
				associazioneDTO.setEtichetta(associazione.getRuoloMensa() != null && associazione.getRuoloMensa().getDescrizioneRuoloMensa() != null ? 
						                     "\"" + associazione.getRuoloMensa().getDescrizioneRuoloMensa().toUpperCase() + "\"" : null);
				
				associazioneDTO.setOrdineFirma(associazione.getOrdineFirma());
				associazioneDTO.setRuolo(associazione.getRuoloMensa() != null ? associazione.getRuoloMensa().getDescrizioneRuoloMensa() : null);
				associazioneDTO.setIdRuolo(associazione.getRuoloMensa() != null ? associazione.getRuoloMensa().getCodiceRuoloMensa() : null);
				
				listaAssociazioniDTO.add(associazioneDTO);
				
				
			}
		}
		
		return listaAssociazioniDTO;
	}

	@Override
	public void modificaFirme(FirmaDTO firma) throws ParseException 
	{
		logger.info("Servizio per la modifica delle firme");
		firmaDAO.modificaFirme(firma);
		
	}

	

}
