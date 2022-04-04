package it.gesev.mensa.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;

import it.gesev.mensa.dao.ReportDAO;
import it.gesev.mensa.dto.PastiConsumatiDTO;

@Service
public class ReportServiceImpl implements ReportService
{
	@Autowired
	private ReportDAO reportDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	/* Carica pasti consumati CSV*/
	@Override
	public void caricaPastiConsumatiCSV(MultipartFile multipartFile) throws IllegalStateException, IOException, ParseException, org.apache.el.parser.ParseException 
	{    
		
        Reader reader = new InputStreamReader(multipartFile.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).build();
       
        List<PastiConsumatiDTO> listaPastiConsumatiCSV = new CsvToBeanBuilder(csvReader)
                .withType(PastiConsumatiDTO.class)
                .build()
                .parse();
        
       
        reportDAO.caricaPastiConsumati(listaPastiConsumatiCSV);

       //logger.info(listaPastiConsumatiCSV + "");
		
	}

	@Override
	public int caricaPastiConsumatiJson(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException
	{
		reportDAO.caricaPastiConsumati(listaPastiConsumatiCSV);
		return 1;
		
	}

}
