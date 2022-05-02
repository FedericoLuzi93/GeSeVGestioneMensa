package it.gesev.mensa.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import it.gesev.mensa.dao.MensaDAO;
import it.gesev.mensa.dao.ReportDAO;
import it.gesev.mensa.dao.ReportDAOTemporaneo;
import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FileDC4DTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.jasper.FirmaDC4Jasper;
import it.gesev.mensa.jasper.GiornoJasper;
import it.gesev.mensa.jasper.ReportGeSeV3Jasper;
import it.gesev.mensa.utils.MensaUtils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceTemporaneoImpl implements ReportServiceTemporaneo
{
	private static final Logger logger = LoggerFactory.getLogger(ReportServiceTemporaneo.class);

	@Value("${gesev.data.format}")
	private String dateFormat;

	@Value("${gesev.italian.data.format}")
	private String dateFormatItalian;
	
	@Autowired
	private ReportDAOTemporaneo reportDAOTemporaneo;
	
	/* Download File Report Gesev 3 */
	@Override
	public FileDC4DTO downloadReportGesev3(DC4RichiestaDTO dc4RichiestaDTO) throws FileNotFoundException, ParseException 
	{
		logger.info("Accesso a downloadReportGesev3 classe ReportServiceTemporaneoImpl");
		List<ReportGeSeV3Jasper> listaReportGesev3 = reportDAOTemporaneo.downloadReportGesev3(dc4RichiestaDTO);
		List<FirmeDC4> listaFirmeReportGesev3 = reportDAOTemporaneo.listaFirmeReportGesev3(dc4RichiestaDTO);

		FileDC4DTO fileDC4DTO = new FileDC4DTO();
		File mockFile = ResourceUtils.getFile("classpath:ReportGeSev3.jrxml");
		
		logger.info("Generazione report in corso...");
		try
		{
			List<GiornoJasper> listaGJ = new ArrayList<GiornoJasper>();
			List<FirmaDC4Jasper> listaFirme = new ArrayList<>();
			
			//Giorno
			int day = 0;
			int max = listaReportGesev3.size();
			for(int i = 0; i < max; i++)
			{
				day++;
				GiornoJasper gJ = new GiornoJasper(day);
				listaGJ.add(gJ);
			}
			
			//Firme
			for(FirmeDC4 firme : listaFirmeReportGesev3)
			{
				FirmaDC4Jasper fJDC4Jasper = new FirmaDC4Jasper();
				fJDC4Jasper.setDescrizioneFirmaDC4(firme.getDescrizione());
				fJDC4Jasper.setNomeFirmaDC4(firme.getNome());
				fJDC4Jasper.setCognomeFirmaDC4(firme.getCognome());

				listaFirme.add(fJDC4Jasper);
			}
			
			//Riempimento tabella
			JRBeanCollectionDataSource JRBlistaReportGesev3 = new JRBeanCollectionDataSource(listaReportGesev3);
			JRBeanCollectionDataSource JRBlistaGiorni = new JRBeanCollectionDataSource(listaGJ);
			JRBeanCollectionDataSource JRBlistaFirme = new JRBeanCollectionDataSource(listaFirme);

			//Assegnazione oggetti
			Map<String, Object> parameters = new HashMap<>();
			
			parameters.put("tabPasti", JRBlistaReportGesev3);
			parameters.put("tabGiorni", JRBlistaGiorni);
			parameters.put("tabFirme", JRBlistaFirme);
			
			parameters.put("anno", dc4RichiestaDTO.getAnno());
			String mese = MensaUtils.convertiMese(dc4RichiestaDTO.getMese());
			parameters.put("mese", mese);
			

			//Stampa
			JasperReport report = JasperCompileManager.compileReport(mockFile.getAbsolutePath());
			JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			byte[] arrayb = JasperExportManager.exportReportToPdf(print);
			fileDC4DTO.setFileDC4(arrayb);
			fileDC4DTO.setNomeFile("Report_GeSeV_3_" + dc4RichiestaDTO.getMese() + "_" + dc4RichiestaDTO.getAnno() + ".pdf"); 
			
		}
		catch(Exception e)
		{
			logger.info("si è verificata un eccezione", e);
		}
		logger.info("Report generato con successo");
		
		return fileDC4DTO;

	}

}
