package it.gesev.mensa.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dao.PrenotazioneDAO;
import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.dto.IdentificativoSistemaDTO;
import it.gesev.mensa.dto.PrenotazioneDTO;
import it.gesev.mensa.dto.TipoDietaDTO;
import it.gesev.mensa.dto.TipoPastoDTO;
import it.gesev.mensa.dto.TipoRazioneDTO;

@Service
public class PrenotazioneServiceImpl implements PrenotazioneService {
	private static Logger logger = LoggerFactory.getLogger(PrenotazioneServiceImpl.class);
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Autowired
	private PrenotazioneDAO prenotazioneDAO;
		
	@Override
	public void insertPrenotazioni(MultipartFile file) throws IOException 
	{
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		
		for (Row row : sheet)
		{
			PrenotazioneDTO prenotazione = new PrenotazioneDTO();
						
			IdentificativoSistemaDTO identificativo = new IdentificativoSistemaDTO();
			identificativo.setIdSistema(row.getCell(0).getStringCellValue());
			prenotazione.setIdentificativoSistema(identificativo);
			
			EnteDTO ente = new EnteDTO();
			ente.setIdEnte(Double.valueOf(row.getCell(1).getNumericCellValue()).intValue());
			prenotazione.setEnte(ente);
			
			Date dataPrenotazione = row.getCell(2).getDateCellValue();
			SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
			prenotazione.setDataPrenotazione(formatter.format(dataPrenotazione));
			
			prenotazione.setCodiceFiscale(row.getCell(3).getStringCellValue());
			
			TipoPastoDTO tipoPasto = new TipoPastoDTO();
			tipoPasto.setCodiceTipoPasto(Double.valueOf(row.getCell(4).getNumericCellValue()).intValue());
			prenotazione.setTipoPasto(tipoPasto);
			
			prenotazione.setFlagCestino(row.getCell(5).getStringCellValue());
			
			TipoDietaDTO tipoDieta = new TipoDietaDTO();
			tipoDieta.setIdTipoDieta(Double.valueOf(row.getCell(6).getNumericCellValue()).intValue());
			prenotazione.setTipoDieta(tipoDieta);
			
			TipoRazioneDTO tipoRazione = new TipoRazioneDTO();
			tipoRazione.setIdTipoRazione(Double.valueOf(row.getCell(7).getNumericCellValue()).intValue());
			prenotazione.setTipoRazione(tipoRazione);
			
			prenotazioneDAO.insertPrenotazione(Arrays.asList(prenotazione));
			
			logger.info("Fine");
				
//				switch (cell.getCellType()) 
//				{
//	            case STRING: 
//	            	logger.info("String");
//	            	break;
//	            case NUMERIC:
//	            	logger.info("Numeric");
//	            	break;
//				case BLANK:
//					logger.info("Blank");
//					break;
//				case BOOLEAN:
//					logger.info("Boolean");
//					break;
//				case ERROR:
//					logger.info("Error");
//					break;
//				case FORMULA:
//					logger.info("Formula");
//					break;
//				case _NONE:
//					logger.info("None");
//					break;
//				default:
//					logger.info("Default");
//					break;
//				}
			
		}
		
	}

}
