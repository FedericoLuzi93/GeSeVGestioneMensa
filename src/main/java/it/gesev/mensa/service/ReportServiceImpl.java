package it.gesev.mensa.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;

import it.gesev.mensa.dao.ReportDAO;
import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaAllegatoCDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FileDC4DTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.dto.IdentificativoSistemaDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.entity.IdentificativoSistema;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.jasper.FirmaDC4Jasper;
import it.gesev.mensa.jasper.FirmaJasper;
import it.gesev.mensa.jasper.ForzaEffettivaJasper;
import it.gesev.mensa.jasper.GiornoJasper;
import it.gesev.mensa.jasper.NumeroPastiGraduatiJasper;
import it.gesev.mensa.jasper.NumeroPastiUFCJasper;
import it.gesev.mensa.jasper.PastoConsumatoJasper;
import it.gesev.mensa.jasper.PastoOrdinatoJasper;
import it.gesev.mensa.utils.IdentificativoSistemaMapper;
import it.gesev.mensa.utils.MensaMapper;
import it.gesev.mensa.utils.MensaUtils;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements ReportService
{
	@Autowired
	private ReportDAO reportDAO;

	@Value("${gesev.data.format}")
	private String dateFormat;

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	/* Carica pasti consumati CSV */
	@Override
	public void caricaPastiConsumatiCSV(MultipartFile multipartFile) throws IllegalStateException, IOException, ParseException, org.apache.el.parser.ParseException 
	{    
		logger.info("Accesso a caricaPastiConsumatiCSV classe ReportServiceImpl");
		Reader reader = new InputStreamReader(multipartFile.getInputStream());
		CSVReader csvReader = new CSVReaderBuilder(reader).build();

		List<PastiConsumatiDTO> listaPastiConsumatiCSV = new CsvToBeanBuilder(csvReader)
				.withType(PastiConsumatiDTO.class)
				.build()
				.parse();

		reportDAO.caricaPastiConsumati(listaPastiConsumatiCSV);		
	}

	/* Carica pasti consumati JSON */ 
	@Override
	public int caricaPastiConsumatiJson(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException
	{
		logger.info("Accesso a caricaPastiConsumatiJson classe ReportServiceImpl");
		reportDAO.caricaPastiConsumati(listaPastiConsumatiCSV);
		return 1;	
	}

	/* Richiesta documento DC4 */
	@Override
	public List<DC4TabellaDTO> richiestaDocumentoDC4(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException 
	{
		logger.info("Accesso a richiestaDocumentoDC4 classe ReportServiceImpl");
		return reportDAO.richiestaDocumentoDC4(dc4RichiestaDTO, true, true, true);
	}

	/* Download File DC4 */
	@Override
	public FileDC4DTO downloadDC4(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException 
	{
		List<DC4TabellaDTO> listaDC4TabellaDTO = reportDAO.richiestaDocumentoDC4(dc4RichiestaDTO, true, true, true);
		List<FirmeDC4> listaFirmeDC4 = reportDAO.richiestaFirmeDC4(dc4RichiestaDTO);
		FileDC4DTO fileDC4DTO = new FileDC4DTO();
		//String filePath = "C:\\Users\\alien\\OneDrive\\Desktop\\Gene\\Pitech\\Git\\Gesev\\GeSeV GestioneMensa\\GeSeV-GestioneMensa\\src\\main\\resources\\DC4.jrxml";
		File mockFile = ResourceUtils.getFile("classpath:DC4.jrxml");
		try
		{
			List<FirmaJasper> listaFJ= new ArrayList<>();
			List<FirmaDC4Jasper> listaFirme = new ArrayList<>();
			List<GiornoJasper> listaGJ = new ArrayList<>();
			List<ForzaEffettivaJasper> listaFeJ = new ArrayList<>();
			List<PastoConsumatoJasper> listaPcJ = new ArrayList<>();
			List<PastoOrdinatoJasper> listaPoJ = new ArrayList<>();
			
			for(DC4TabellaDTO dc4Tab : listaDC4TabellaDTO)
			{
				FirmaJasper fJ = new FirmaJasper();
				ForzaEffettivaJasper feJ = new ForzaEffettivaJasper();
				PastoConsumatoJasper pcJ = new PastoConsumatoJasper();
				PastoOrdinatoJasper poJ = new PastoOrdinatoJasper();

				fJ.setFirma(dc4Tab.getFirma());

				feJ.setColazioneEF(dc4Tab.getColazioneEffettiva().toString());
				feJ.setPranzoEF(dc4Tab.getPranzoEffettiva().toString());
				feJ.setCenaEF(dc4Tab.getCenaEffettiva().toString());

				pcJ.setColazionePC(dc4Tab.getColazioneConsumati().toString());
				pcJ.setPranzoPC(dc4Tab.getPranzoConsumati().toString());
				pcJ.setCenaPC(dc4Tab.getCenaConsumati().toString());

				poJ.setColazionePO(dc4Tab.getColazioneOrdinati().toString());
				poJ.setPranzoPO(dc4Tab.getPranzoOrdinati().toString());
				poJ.setCenaPO(dc4Tab.getCenaOridnati().toString());

				listaFJ.add(fJ);
				listaFeJ.add(feJ);
				listaPcJ.add(pcJ);
				listaPoJ.add(poJ);
				
			}

			//Firme
			for(FirmeDC4 firme : listaFirmeDC4)
			{
				FirmaDC4Jasper fJDC4Jasper = new FirmaDC4Jasper();
				fJDC4Jasper.setDescrizioneFirmaDC4(firme.getDescrizione());
				fJDC4Jasper.setNomeFirmaDC4(firme.getNome());
				fJDC4Jasper.setCognomeFirmaDC4(firme.getCognome());
			
				listaFirme.add(fJDC4Jasper);
			}

			//Giorni
			int day = 0;
			int max = listaDC4TabellaDTO.size();
			for(int i = 0; i < max; i++)
			{
				day++;
				GiornoJasper gJ = new GiornoJasper(day);
				listaGJ.add(gJ);
			}

			//Riempimento tabella
			JRBeanCollectionDataSource JRBlistaFE = new JRBeanCollectionDataSource(listaFeJ);
			JRBeanCollectionDataSource JRBlistaPO = new JRBeanCollectionDataSource(listaPoJ);
			JRBeanCollectionDataSource JRBlistaPC = new JRBeanCollectionDataSource(listaPcJ);
			JRBeanCollectionDataSource JRBlistaFirme = new JRBeanCollectionDataSource(listaFJ);
			JRBeanCollectionDataSource JRBlistaGiorni = new JRBeanCollectionDataSource(listaGJ);
			JRBeanCollectionDataSource JRBlistaFirmeDC4 = new JRBeanCollectionDataSource(listaFirme);

			//Assegnazione oggetti
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("forzaEffettivaTab", JRBlistaFE);
			parameters.put("pastoOrdinatoTab", JRBlistaPO);
			parameters.put("pastiConsumatiTab", JRBlistaPC);
			parameters.put("firmaTab", JRBlistaFirme);
			parameters.put("giornoTab", JRBlistaGiorni);
			parameters.put("firmeDocuamentiTab", JRBlistaFirmeDC4);

			//Parametri singoli
			String EDRC = listaDC4TabellaDTO.get(0).getDescrizioneEnte();
			parameters.put("EDRC", EDRC);

			String mese = MensaUtils.convertiMese(dc4RichiestaDTO.getMese());
			parameters.put("mese", mese);

			String anno = dc4RichiestaDTO.getAnno();
			parameters.put("anno", anno);

			String gestore = "Antonio Rossi";
			parameters.put("gestore", gestore);

			String sottoufficiale = "Marco Verdi";
			parameters.put("sottoufficiale", sottoufficiale);

			String capoServizio = "Tiziano Pinco";
			parameters.put("capoServizio", capoServizio);

			String rappresentante = "Francesco Pallino";
			parameters.put("rappresentante", rappresentante);

			//Stampa
			JasperReport report = JasperCompileManager.compileReport(mockFile.getAbsolutePath());
			JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			byte[] arrayb = JasperExportManager.exportReportToPdf(print);
			fileDC4DTO.setFileDC4(arrayb);
			fileDC4DTO.setNomeFile("DC4_" + dc4RichiestaDTO.getMese() + "_" + dc4RichiestaDTO.getAnno() + ".pdf"); 
		}
		catch(Exception e)
		{

		}

		return fileDC4DTO;
	}

	/* Richiesta documento DC4 Allegato C */
	@Override
	public List<DC4TabellaAllegatoCDTO> richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException 
	{
		logger.info("Accesso a richiestaDocumentoDC4 classe ReportServiceImpl");
		return reportDAO.richiestaDocumentoDC4AllegatoC(dc4RichiestaDTO);
	}

	/* Download documento DC4 Allegato C */
	@Override
	public FileDC4DTO downloadDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO) throws FileNotFoundException 
	{
		List<DC4TabellaAllegatoCDTO> listaDc4TabellaAllegatoCDTO = reportDAO.richiestaDocumentoDC4AllegatoC(dc4RichiestaDTO);
		FileDC4DTO fileDC4DTO = new FileDC4DTO();
		File mockFile = ResourceUtils.getFile("classpath:DC4AllegatoC.jrxml");

		try
		{
			List<NumeroPastiUFCJasper> listaPastiUFCJ = new ArrayList<>();
			List<NumeroPastiGraduatiJasper> listaPastiGraduatiJ = new ArrayList<>();
			List<GiornoJasper> listaGJ = new ArrayList<>();

			for(DC4TabellaAllegatoCDTO dc4Tab : listaDc4TabellaAllegatoCDTO)
			{
				NumeroPastiUFCJasper pastoUFCJ = new NumeroPastiUFCJasper();
				NumeroPastiGraduatiJasper pastoGraduatoJ = new NumeroPastiGraduatiJasper();

				pastoUFCJ.setnPranziT1(dc4Tab.getNumpranziUSC().toString());
				pastoUFCJ.setnCeneT1(dc4Tab.getNumCeneUSC().toString());

				pastoGraduatoJ.setnColazioniT2(dc4Tab.getNumColazioniGraduati().toString());
				pastoGraduatoJ.setnPranziT2(dc4Tab.getNumPranziGraduati().toString());
				pastoGraduatoJ.setnCeneT2(dc4Tab.getNumCeneGraduati().toString());

				listaPastiUFCJ.add(pastoUFCJ);
				listaPastiGraduatiJ.add(pastoGraduatoJ);
			}

			int day = 0;
			int max = listaDc4TabellaAllegatoCDTO.size();
			for(int i = 0; i < max; i++)
			{
				day++;
				GiornoJasper gJ = new GiornoJasper(day);
				listaGJ.add(gJ);
			}

			//Riempimento tabella
			JRBeanCollectionDataSource JRBlistaPastiUff = new JRBeanCollectionDataSource(listaPastiUFCJ);
			JRBeanCollectionDataSource JRBlistaPastiTruppa = new JRBeanCollectionDataSource(listaPastiGraduatiJ);
			JRBeanCollectionDataSource JRBlistaGiorni1 = new JRBeanCollectionDataSource(listaGJ);
			JRBeanCollectionDataSource JRBlistaGiorni2 = new JRBeanCollectionDataSource(listaGJ);

			//Assegnazione oggetti
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("Tab1", JRBlistaPastiUff);
			parameters.put("Tab2", JRBlistaPastiTruppa);
			parameters.put("giornoTab", JRBlistaGiorni1);
			parameters.put("giornoTab2", JRBlistaGiorni2);

			//Parametri singoli
			String mese = MensaUtils.convertiMese(dc4RichiestaDTO.getMese());
			parameters.put("mese", mese);

			//Stampa
			JasperReport report = JasperCompileManager.compileReport(mockFile.getAbsolutePath());
			JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			byte[] arrayb = JasperExportManager.exportReportToPdf(print);
			fileDC4DTO.setFileDC4(arrayb);
			fileDC4DTO.setNomeFile("DC4_AllegatoC_" + dc4RichiestaDTO.getMese() + "_" + dc4RichiestaDTO.getAnno() + ".pdf"); 

		}
		catch(Exception e)
		{

		}
		return fileDC4DTO;

	}

	
	/* Leggi tutti identificativi Sistema */
	@Override
	public List<IdentificativoSistemaDTO> getAllIdentificativiSistema() 
	{
		logger.info("Accesso a getAllIdentificativiSistem, classe ReportServiceImpl");
		List<IdentificativoSistema> listaIdentificativoSistema = reportDAO.getAllIdentificativiSistema();
		List<IdentificativoSistemaDTO> listaIdentificativoSistemaDTO = new ArrayList<>();
		logger.info("Inizio ciclo For in getAllIdentificativiSistem classe ReportServiceImpl");
		for(IdentificativoSistema is : listaIdentificativoSistema)
		{
			IdentificativoSistemaDTO isDTO = null;
			isDTO = IdentificativoSistemaMapper.mapToDTO(is);
			listaIdentificativoSistemaDTO.add(isDTO);
		}
		logger.info("Fine getAllIdentificativiSistem classe ReportServiceImpl");
		return listaIdentificativoSistemaDTO;
	}

}
