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
import it.gesev.mensa.dto.FirmaDTO;
import it.gesev.mensa.dto.ReportDTO;
import it.gesev.mensa.dto.TipoReportDTO;
import it.gesev.mensa.entity.AssReportRuoloMensa;
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
		logger.info("Servizio per la generazione del dettaglio deelle firme...");
		
		DettaglioReportDTO dettaglio = new DettaglioReportDTO();
		ModelMapper mapper = new ModelMapper();
		
		List<Report> listaReport = firmaDAO.getListaReport(tipoReport.getCodiceTipoReport());
		List<AssReportRuoloMensa> listaAssociazioni = firmaDAO.getReportRuolo();
		List<TipoReport> listaTipiReport = firmaDAO.getTipiReport();
		
		if(listaReport != null && listaReport.size() > 0)
		{
			List<ReportDTO> listaReportDTO = new ArrayList<>();
			for(Report report : listaReport)
			{
				listaReportDTO.add(mapper.map(report, ReportDTO.class));
			}
			
			dettaglio.setListaReport(listaReportDTO);
		}
		
		if(listaAssociazioni != null && listaAssociazioni.size() > 0)
		{
			List<AssReportRuoloMensaDTO> listaAssociazioniDTO = new ArrayList<>();
			for(AssReportRuoloMensa associazione : listaAssociazioni)
			{
				AssReportRuoloMensaDTO dto = new AssReportRuoloMensaDTO();
				dto.setAssReportRuoloMensaId(associazione.getAssReportRuoloMensaId());
				dto.setRuolo(associazione.getRuoloMensa() != null ? associazione.getRuoloMensa().getDescrizioneRuoloMensa() : null);
				
				listaAssociazioniDTO.add(dto);
			}
			
			dettaglio.setListaAssociazioni(listaAssociazioniDTO);
		}
		
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

}
