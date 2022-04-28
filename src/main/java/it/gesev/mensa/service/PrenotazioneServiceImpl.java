package it.gesev.mensa.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;

import it.gesev.mensa.dao.PrenotazioneDAO;
import it.gesev.mensa.dto.CaricamentoPrenotazioniDTO;
import it.gesev.mensa.dto.CaricamentoPastiConsumatiDTO;
import it.gesev.mensa.dto.PrenotazioneDTO;
import it.gesev.mensa.exc.GesevException;

@Service
public class PrenotazioneServiceImpl implements PrenotazioneService {
	private static Logger logger = LoggerFactory.getLogger(PrenotazioneServiceImpl.class);
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Autowired
	private PrenotazioneDAO prenotazioneDAO;
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void insertPrenotazioni(MultipartFile file) throws IOException 
	{
		logger.info("Inserimento prenotazioni...");
		
		Reader reader = new InputStreamReader(file.getInputStream());
	    CSVReader csvReader = new CSVReaderBuilder(reader).build();
	    
	    List<CaricamentoPrenotazioniDTO> listaPrenotazioni = new CsvToBeanBuilder(csvReader)
                .withType(CaricamentoPrenotazioniDTO.class)
                .build()
                .parse();
	    
	    prenotazioneDAO.insertPrenotazione(listaPrenotazioni);
	    
	    logger.info("Fine inserimento prenotazioni...");
		
	}

	@Override
	public List<PrenotazioneDTO> getListaPrenotazioni(String dataPrenotazione) 
	{
		logger.info("Service per la lista prenotazioni...");
		SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
		Date parsedDate = null;
		
		try
		{	
			parsedDate = formatter.parse(dataPrenotazione);
			
		}
		
		catch(Exception ex)
		{
			throw new GesevException("La data fornita non e' corretta o non e' nel formato atteso AAAA-MM-GG", HttpStatus.BAD_REQUEST);
		}
				
		return prenotazioneDAO.getListaPrenotazioni(parsedDate);
	}

}
